package bsu.famcs.chat.controller;

import java.io.IOException;
import java.io.PrintWriter;

import bsu.famcs.chat.model.Message;
import bsu.famcs.chat.model.IdStorage;

import java.text.ParseException;
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
import javax.xml.xpath.XPathExpressionException;

import jdk.nashorn.internal.parser.JSONParser;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.xml.sax.SAXException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

@WebServlet("/chat")
public class MessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(MessageServlet.class.getName());
    private final Lock _mutex = new ReentrantLock(true);

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
        logger.info("token: " + token);

        if (token != null && !"".equals(token)) {
            int index = getIndex(token);
            logger.info("index " + index);
            if (index < IdStorage.getSize()){
                String messages = formResponse(index);
                logger.info("response messages: " + messages);
                response.setContentType(ServletUtil.APPLICATION_JSON);
                logger.info("response status: " + 200);
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_OK);
                PrintWriter out = response.getWriter();
                out.print(messages);
                out.flush();
            }
            else {
                logger.info("response status: " + 304);
                response.sendError(HttpServletResponse.SC_NOT_MODIFIED, "no new messages");
            }
        } else {
            logger.error("bad request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "'token' parameter needed");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");
        String data = ServletUtil.getMessageBody(request);
        logger.info("data: " + data);
        try {
            JSONObject json = stringToJson(data);
            json.put(METHOD, "POST");
            Message message = jsonToMessage(json);

            IdStorage.addId(message.getId());
            MessageStorage.addMessage(message);

            _mutex.lock();
            XMLHistoryUtil.addId(message.getId());
            XMLHistoryUtil.addMessage(message);
            _mutex.unlock();

            logger.info("response status: " + 200);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (org.json.simple.parser.ParseException | ParserConfigurationException | SAXException | TransformerException e) {
            logger.error("bad request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doDelete");
        String data = ServletUtil.getMessageBody(request);
        logger.info("data: " + data);
        try {
            JSONObject json = stringToJson(data);
            String id = json.get(ID).toString();
            Message messageToUpdate = MessageStorage.getMessageById(id);
            if (messageToUpdate != null) {
                messageToUpdate.setDate(getDate());
                messageToUpdate.setMethod("DELETE");
                messageToUpdate.setText("");

                _mutex.lock();
                XMLHistoryUtil.updateData(messageToUpdate);
                XMLHistoryUtil.addId(id);
                _mutex.unlock();
                IdStorage.addId(id);
                logger.info("response status: " + 200);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                logger.error("bad request");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task does not exist");
            }
        } catch (Exception e) {
            logger.error("bad request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPut");
        String data = ServletUtil.getMessageBody(request);
        logger.info("data: " + data);
        try {
            JSONObject json = stringToJson(data);
            String id = json.get(ID).toString();
            Message messageToUpdate = MessageStorage.getMessageById(id);
            if (messageToUpdate != null) {
                messageToUpdate.setDate(getDate());
                messageToUpdate.setMethod("PUT");
                messageToUpdate.setText(json.get(TEXT).toString());

                _mutex.lock();
                XMLHistoryUtil.updateData(messageToUpdate);
                XMLHistoryUtil.addId(id);
                _mutex.unlock();
                IdStorage.addId(id);
                logger.info("response status: " + 200);

                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                logger.error("bad request");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Task does not exist");
            }
        } catch (Exception e) {
            logger.error("bad request");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private static String getDate() {
        DateFormat formatter;
        formatter = DateFormat.getDateTimeInstance();
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
        return formatter.format(new Date());
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