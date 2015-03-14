package org.ztreamy;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Event {
    private String eventId;
    private String sourceId;
    private String syntax;
    private String timestamp;
    private String applicationId;
    private Map<String, Object> body;
    private Map<String, String> extraHeaders;

    private Charset charsetUTF8 = Charset.forName("UTF-8");

    /*
     * RFC 3339 - Date and Time on the Internet: Timestamps
     * https://www.ietf.org/rfc/rfc3339.txt
     * http://www.iso.org/iso/home/standards/iso8601.htm
     *
     * TODO: Check if it's worth it using Joda-Time
     * http://www.joda.org/joda-time/faq.html#iso
     */
    private static SimpleDateFormat rfc3339Format =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    public Event(String eventId, String sourceId, String syntax,
                 String applicationId, Map<String, Object> body) {
        this.eventId = eventId;
        this.sourceId = sourceId;
        this.syntax = syntax;
        this.applicationId = applicationId;
        this.body = body;
        this.extraHeaders = new LinkedHashMap<String, String>();
        timestamp = createTimestamp();
    }

    public Event(String eventId, String sourceId, String syntax,
                 String applicationId) {
        this(eventId, sourceId, syntax, applicationId, null);
    }

    public Event(String sourceId, String syntax, String applicationId) {
        this(createUUID(), sourceId, syntax, applicationId);
    }

    public void setExtraHeader(String name, String value) {
        extraHeaders.put(name, value);
    }

    public static String createTimestamp() {
        return rfc3339Format.format(new Date());
    }

    /*
     * A Universally Unique IDentifier (UUID) URN Namespace
     * http://www.ietf.org/rfc/rfc4122.txt
     */
    public static String createUUID() {
        return UUID.randomUUID().toString();
    }

    public void setBody(Map<String, Object> bodyAsMap) {
        this.body = bodyAsMap;
    }

    public void setBody(String bodyAsString) {
        body = new LinkedHashMap<String, Object>();
        body.put("value", bodyAsString);
    }

    public byte[] serialize() {
        StringBuffer buffer = new StringBuffer();;
        serializeHeader(buffer, "Event-Id", eventId);
        serializeHeader(buffer, "Source-Id", sourceId);
        serializeHeader(buffer, "Syntax", syntax);
        if (applicationId != null) {
            serializeHeader(buffer, "Application-Id", applicationId);
        }
        serializeHeader(buffer, "Timestamp", timestamp);
        for (Map.Entry<String, String> entry: extraHeaders.entrySet()) {
            serializeHeader(buffer, entry.getKey(), entry.getValue());
        }
        byte[] bodyAsBytes;
        if (syntax.equals("application/json")) {
            bodyAsBytes = bodyAsJSON();
        } else {
            bodyAsBytes = body.get("value").toString().getBytes(charsetUTF8);
        }
        serializeHeader(buffer, "Body-Length", String.valueOf(bodyAsBytes.length));
        // Delimiter of headers and body
        buffer.append("\r\n");
        byte[] headers = buffer.toString().getBytes(charsetUTF8);
        return concatenate(headers, bodyAsBytes);
    }

    public static void serializeHeader(StringBuffer buffer, String key, String value) {
        buffer.append(key);
        buffer.append(": ");
        buffer.append(value);
        buffer.append("\r\n");
    }

    private static byte[] concatenate(byte[] first, byte[] second) {
        byte[] dest = new byte[first.length + second.length];
        System.arraycopy(first, 0, dest, 0, first.length);
        System.arraycopy(second, 0, dest, first.length, second.length);
        return dest;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("Event-Id", eventId);
        data.put("Source-Id", sourceId);
        data.put("Syntax", syntax);
        if (applicationId != null) {
            data.put("Application-Id", applicationId);
        }
        data.putAll(extraHeaders);
        if (syntax.equals("application/json")) {
            data.put("Body", body);
        } else {
            data.put("Body", body.get("value").toString());
        }
        return data;
    }

    private byte[] bodyAsJSON() {
        Gson gson = new Gson();
        return gson.toJson(body).getBytes(charsetUTF8);
    }

    public static void main(String[] args) throws IOException {
        // Create an Event object
        Event event = new Event(createUUID(), "text/plain", "ztreamy-java-test");
        event.setBody("Test body");
        System.out.write(event.serialize());
    }

}
