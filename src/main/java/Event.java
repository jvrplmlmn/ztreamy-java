package org.ztreamy;

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

    public static void main(String[] args) {
        System.out.println("Hello org.ztreamy.Event!");
    }
}
