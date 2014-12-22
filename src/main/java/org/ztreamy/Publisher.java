package org.ztreamy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Publisher {

    private URL serverURL;

    public Publisher(URL serverURL) {
        this.serverURL = serverURL;
    }

    public int publish(Event event) throws IOException{
        HttpURLConnection con = (HttpURLConnection) serverURL.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/ztreamy-event");
        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        byte[] data = event.serialize();
        out.write(data);
        out.close();
        return con.getResponseCode();
    }

    public static void main(String[] args) throws IOException {
        Publisher publisher = new Publisher(new URL("http://localhost:9000/events/publish"));
        String sourceID = Event.createUUID();
        Event event = new Event(sourceID, "text/plain", "ztreamy-java-test");
        int result = publisher.publish(event);
        if (result == 200) {
            System.out.println("An event has just been sent to the server");
        } else {
            System.out.println("The server responded with error " + result);

        }
    }
}