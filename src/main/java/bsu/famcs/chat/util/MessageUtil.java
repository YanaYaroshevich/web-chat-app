package bsu.famcs.chat.util;
import bsu.famcs.chat.model.Message;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import java.lang.Object;

public final class MessageUtil {
    public static final String TOKEN = "token";
    public static final String MESSAGES = "messages";
    private static final String TN = "TN";
    private static final String EN = "EN";
    private static final String TEXT = "text";
    private static final String NAME = "name";

    private MessageUtil() {
    }

    public static String getToken(int index) {
        Integer number = index * 8 + 11;
        return TN + number + EN;
    }

    public static int getIndex(String token) {
        return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
    }

    private static String getDate() {
        DateFormat formatter;
        formatter = DateFormat.getDateTimeInstance();
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
        return formatter.format(new Date());
    }

    public static String getUniqueId(){
        Date date = new Date();
        Random rand = new Random(date.getTime());
        return ((Integer)Math.abs(rand.nextInt() * rand.nextInt())).toString();
    }

    public static JSONObject stringToJson(String data) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(data.trim());
    }

    public static Message jsonToMessage(JSONObject json) {
        Object id = getUniqueId();
        Object text = json.get(TEXT);
        Object name = json.get(NAME);
        Object date = getDate();

        if (text != null && name != null) {
            return new Message((String) name, (String) text, (String) date, (String) id, false, false);
        }
        return null;
    }
}
