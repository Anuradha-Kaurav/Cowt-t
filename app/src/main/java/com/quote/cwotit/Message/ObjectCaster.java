package com.quote.cwotit.Message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Palo12 on 04-05-2017.
 */
public class ObjectCaster {


    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public static <T> T jSONcast(Class<T> clazz, String jsonInString) {
        //JSON from String to Object
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonInString, clazz);
        } catch (IOException ex) {
            return null;
        }
    }

    public static <T> T jSONcast(Class<T> clazz, File jsonInFile) throws IOException {
        //JSON from file to Object
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonInFile, clazz);
    }

    public static <T> T jSONcast(Class<T> clazz, URL jsonInURL) throws IOException {
        //JSON from URL to Object
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonInURL, clazz);
    }
    public static <T> T cast(Class<T> clazz, Object object) {
        try {
            T castedObject = null;
            if (object == null) {
                return null;
            }
            if (clazz.isInstance(object)) {
                castedObject = clazz.cast(object);
            } else {
                String val = String.valueOf(object);
                castedObject = newInstance(clazz, val);
            }
            return castedObject;
        } catch (ClassCastException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    private static <T> T newInstance(Class<T> clazz, String val) {
        if (clazz.isAssignableFrom(BigDecimal.class)) {
            return (T) new BigDecimal(val);
        } else if (clazz.isAssignableFrom(Integer.class)) {
            return (T) new Integer(val);
        } else if (clazz.isAssignableFrom(Date.class)) {
            try {
                return (T) new Date(val);
            } catch (Exception ex) {
                return (T) parseDate(val);
            }
        }
        return (T) val;
    }

    public static Date parseDate(String date) {
        if (date == null) {
            return new Date();
        }
        try {
            return format.parse(date);
        } catch (ParseException ex) {
            return null;
        }
    }

    public static Object g(Map map){
//        JSONObject object = new JSONObject(map);
//        ObjectMapper w = new ObjectMapper();
        String mJsonString = null;
        JsonParser parser = new JsonParser();
        JsonElement mJson =  parser.parse(mJsonString);
        Gson gson = new Gson();
//        gson.
        Object object = gson.fromJson(mJson, Object.class);
       return  object;

    }


}
