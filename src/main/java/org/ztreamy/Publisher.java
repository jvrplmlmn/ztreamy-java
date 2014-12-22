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
        // Check arguments
        if (args.length < 1 || args.length > 2) {
            System.err.println("CLI parameter expected");
            System.err.println("java Publisher <publication URL>");
            System.exit(1);
        }

        // Publisher
        Publisher publisher = new Publisher(new URL(args[0]));

        // Event
        String sourceID = Event.createUUID();
        Event event = new Event(sourceID, "text/plain", "ztreamy-java-test");

        // Publish event and process the result
        int result = publisher.publish(event);
        if (result == 200) {
            System.out.println("An event has just been sent to the server");
        } else {
            System.out.println("The server responded with error " + result);

        }
    }
}