package org.ztreamy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Event {
    private String eventId;
    private String sourceId;
    private String syntax;
    private String timestamp;
    private String applicationId;
    private byte[] body;
    private Map<String, String> extraHeaders;

    private Charset charsetUTF8 = Charset.forName("UTF-8");
    private static SimpleDateFormat rfc3339Format =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    public Event(String eventId, String sourceId, String syntax,
                 String applicationId, byte[] body) {
        this.eventId = eventId;
        this.sourceId = sourceId;
        this.syntax = syntax;
        this.applicationId = applicationId;
        this.body = body;
        this.extraHeaders = new HashMap<String, String>();
        timestamp = createTimestamp();
    }

    public Event(String eventId, String sourceId, String syntax,
                 String applicationId) {
        this(eventId, sourceId, syntax, applicationId, new byte[0]);
    }

    public Event(String sourceId, String syntax, String applicationId) {
        this(createUUID(), sourceId, syntax, applicationId);
    }

    public static String createTimestamp() {
        return rfc3339Format.format(new Date());

    }

    public static String createUUID() {
        return UUID.randomUUID().toString();
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setBody(String bodyAsString) {
        this.body = bodyAsString.getBytes(charsetUTF8);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello org.ztreamy.Event!");

        // Create an Event object
        Event event = new Event(createUUID(), "text/plain", "ztreamy-java-test");

        // Create a buffer and write the Event-Id header
        StringBuffer buffer = new StringBuffer();
        buffer.append("Event-Id");
        buffer.append(": ");
        buffer.append(event.eventId);
        buffer.append("\r\n");

        byte[] header = buffer.toString().getBytes(event.charsetUTF8);

        // Print the header
        System.out.write(header);
    }

}
