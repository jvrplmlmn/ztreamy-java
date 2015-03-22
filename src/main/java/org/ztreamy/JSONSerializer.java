package org.ztreamy;


import com.google.gson.Gson;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JSONSerializer implements Serializer {

    private Gson gson;

    public JSONSerializer() {
        gson = new Gson();
    }

    private static Charset charsetUTF8 = Charset.forName("UTF-8");


    @Override
    public String contentType() {
        return "application/json";
    }

    @Override
    public byte[] serialize(Event event) {
        return gson.toJson(event.toMap()).getBytes(charsetUTF8);
    }

    @Override
    public byte[] serialize(Event[] events) {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for (Event event: events) {
            maps.add(event.toMap());
        }
        return gson.toJson(maps).getBytes(charsetUTF8);
    }
}