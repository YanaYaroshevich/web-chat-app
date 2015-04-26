package bsu.famcs.chat.controller;

import java.io.IOException;
import java.io.PrintWriter;

import bsu.famcs.chat.model.Message;
import bsu.famcs.chat.model.MessageStorage;
import bsu.famcs.chat.storage.XMLHistoryUtil;
import bsu.famcs.chat.util.ServletUtil;

import static bsu.famcs.chat.util.MessageUtil.*;
import static bsu.famcs.chat.util.ServletUtil.APPLICATION_JSON;
import static bsu.famcs.chat.util.ServletUtil.getMessageBody;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

@WebServlet("/chat")
public class MessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(MessageServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try {
            loadHistory();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");
        String token = request.getParameter(TOKEN);
        logger.info("Token " + token);

        if (token != null && !"".equals(token)) {
            int index = getIndex(token);
            logger.info("Index " + index);
            String messages = formResponse(index);
            response.setContentType(ServletUtil.APPLICATION_JSON);
            PrintWriter out = response.getWriter();
            out.print(messages);
            out.flush();
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "'token' parameter needed");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");
        String data = ServletUtil.getMessageBody(request);
        logger.info(data);
        try {
            JSONObject json = stringToJson(data);
            Message message = jsonToMessage(json);
            MessageStorage.addMessage(message);
            XMLHistoryUtil.addMessage(message);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @SuppressWarnings("unchecked")
    private String formResponse(int index) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MESSAGES, MessageStorage.getSubMessagesByIndex(index));
        jsonObject.put(TOKEN, getToken(MessageStorage.getSize()));
        return jsonObject.toJSONString();
    }

    private void loadHistory() throws SAXException, IOException, ParserConfigurationException, TransformerException  {
        if (XMLHistoryUtil.doesStorageExist()) {
            MessageStorage.addAll(XMLHistoryUtil.getMessages());
        } else {
            XMLHistoryUtil.createStorage();
        }
    }
}
