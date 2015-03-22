package org.ztreamy;

import org.apache.http.HttpStatus;

import java.io.FileOutputStream;
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
        con.setRequestProperty("Content-Type", serializer.contentType());
        con.setDoOutput(true);
        OutputStream out = con.getOutputStream();
        OutputStream log = null;
        if (logFileName != null) {
            // Append
            log = new FileOutputStream(logFileName, true);
        }
        byte[] data;
        if (events.length == 1) {
            data = serializer.serialize(events[0]);
        } else {
            data = serializer.serialize(events);
        }
        out.write(data);
        if (logFileName != null) {
            log.write(data);
        }
        out.close();
        if (logFileName != null) {
            log.close();
        }
        return con.getResponseCode();
    }

    public int publish(Event event) throws IOException {
        return publish(new Event[] {event});
    }

    public static void main(String[] args) throws IOException {
        // Check arguments
        if (args.length < 2 || args.length > 3) {
            System.err.println("CLI parameter expected");
            System.err.println("java Publisher <publication URL> <number of events> [data log file]");
            System.exit(1);
        }

        // Publisher
        Publisher publisher;
        Serializer serializer = new JSONSerializer();
        if (args.length == 2) {
            publisher = new Publisher(new URL(args[0]), serializer);
        } else {
            publisher = new Publisher(new URL(args[0]), args[2], serializer);
        }

        // Publish N events
        int numEvents = Integer.parseInt(args[1]);

        // All the events have the same Source-Id
        String sourceID = Event.createUUID();

        for (int i = 0; i < numEvents; i++) {
            // Publish event
            int result = publisher.publish(new DemoEvent(sourceID));
            // Process the result
            if (result == HttpStatus.SC_OK) {
                System.out.println("An event has just been sent to the server");
            } else {
                System.out.println("The server responded with error " + result);

            }

        }

    }
}
