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

    public int publish(Event[] events) throws IOException{
        HttpURLConnection con = (HttpURLConnection) serverURL.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/ztreamy-event");
        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        for (Event event: events) {
            byte[] data = event.serialize();
            out.write(data);
        }
        out.close();
        return con.getResponseCode();
    }

    public int publish(Event event) throws IOException {
        return publish(new Event[] {event});
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

        // All the events have the same Source-Id
        String sourceID = Event.createUUID();

        // Publish N events
        int numEvents = Integer.parseInt(args[1]);
        for (int i = 0; i < numEvents; i++) {
            Event event = new Event(sourceID, "text/plain", "ztreamy-java-test");
            // Publish event
            int result = publisher.publish(event);
            // Process the result
            if (result == 200) {
                System.out.println("An event has just been sent to the server");
            } else {
                System.out.println("The server responded with error " + result);

            }

        }

    }
}