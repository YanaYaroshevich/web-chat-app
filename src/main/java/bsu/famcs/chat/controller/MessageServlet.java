package bsu.famcs.chat.controller;

import java.io.IOException;
import java.io.PrintWriter;

import bsu.famcs.chat.model.Message;
import bsu.famcs.chat.model.IdStorage;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import bsu.famcs.chat.model.MessageStorage;
import bsu.famcs.chat.storage.XMLHistoryUtil;
import bsu.famcs.chat.util.ServletUtil;

import static bsu.famcs.chat.util.MessageUtil.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
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
        System.out.println("doPost");
        String data = ServletUtil.getMessageBody(request);
        System.out.println("doPost");
        logger.info(data);
        System.out.println("doPost");
        try {
            System.out.println("doPost");
            JSONObject json = stringToJson(data);
            System.out.println("doPost");
            json.put(METHOD, "POST");
            System.out.println("doPost");
            Message message = jsonToMessage(json);
            System.out.println("doPost");

            IdStorage.addId(message.getId());
            XMLHistoryUtil.addId(message.getId());

            MessageStorage.addMessage(message);
            XMLHistoryUtil.addMessage(message);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            logger.error(e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private List<Message> difference(List<String> ids){
        List<Message> difference = Collections.synchronizedList(new ArrayList<Message>());
        for (int i = 0; i < ids.size(); i++){
            Message curMsg = MessageStorage.getMessageById(ids.get(i));
            if(!difference.contains(curMsg))
                difference.add(curMsg);
        }
        return difference;
    }

    @SuppressWarnings("unchecked")
    private String formResponse(int index) {
        JSONObject jsonObject = new JSONObject();
        List<String> ids = IdStorage.getSubIdsByIndex(index);
        jsonObject.put(MESSAGES, difference(ids));
        jsonObject.put(TOKEN, getToken(IdStorage.getSize()));
        return jsonObject.toJSONString();
    }

    private void loadHistory() throws SAXException, IOException, ParserConfigurationException, TransformerException  {
        if (XMLHistoryUtil.doesStorageExist()) {
            List<Message> messageList = XMLHistoryUtil.getMessages();
            MessageStorage.addAll(messageList);
            for (Message msg : messageList){
                System.out.println(msg.getDate() + " " + msg.getName() + ": " + msg.getText());
            }
        } else {
            XMLHistoryUtil.createStorage();
        }
        if (XMLHistoryUtil.doesIdStorageExist()){
            List<String> idList = XMLHistoryUtil.getIds();
            IdStorage.addAll(idList);
        } else {
            XMLHistoryUtil.createIdStorage();
        }
    }
}