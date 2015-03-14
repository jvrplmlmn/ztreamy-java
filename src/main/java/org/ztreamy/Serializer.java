package org.ztreamy;

public interface Serializer {
    String contentType();
    byte[] serialize(Event event);
    byte[] serialize(Event[] events);
}
