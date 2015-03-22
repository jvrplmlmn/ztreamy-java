package org.ztreamy;

import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Publisher {

    private URL serverURL;
    private String logFileName;
    private Serializer serializer;

    public Publisher(URL serverURL) {
        this(serverURL, null, new ZtreamySerializer());
    }

    public Publisher(URL serverURL, String logFileName) {
        this(serverURL, logFileName, new ZtreamySerializer());
    }

    public Publisher(URL serverURL, Serializer serializer) {
        this(serverURL, null, serializer);
    }

    public Publisher(URL serverURL, String logFileName, Serializer serializer) {
        this.serverURL = serverURL;
        this.logFileName = logFileName;
        this.serializer = serializer;
    }

    public int publish(Event[] events) throws IOException{
        HttpURLConnection con = (HttpURLConnection) serverURL.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/ztreamy-event");
        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        byte[] data = serializer.serialize(events);
        out.write(data);
        out.close();
        return con.getResponseCode();
    }

    public int publish(Event event) throws IOException {
        return publish(new Event[] {event});
    }

    public static void main(String[] args) throws IOException {
        // Check arguments
        if (args.length < 2 || args.length > 3) {
            System.err.println("CLI parameter expected");
            System.err.println("java Publisher <publication URL> <number of events>");
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
            if (result == HttpStatus.SC_OK) {
                System.out.println("An event has just been sent to the server");
            } else {
                System.out.println("The server responded with error " + result);

            }

        }

    }
}
