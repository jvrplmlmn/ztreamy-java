package org.ztreamy;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Consumer {

    private URL serverURL;

    public Consumer(URL serverURL) {
        this.serverURL = serverURL;
    }

    public void receive_events() throws IOException {
        System.out.println("Receiving events");
        URLConnection connection = serverURL.openConnection();
        System.out.println(connection);
        connection.setDoOutput(true);

        DataInputStream dis = new DataInputStream(connection.getInputStream());

        String inputLine;

        while ((inputLine = dis.readLine()) != null) {
            System.out.println(inputLine);
        }
        dis.close();
    }

    public static void main(String[] args) throws IOException {
        // Check arguments
        if (args.length != 1) {
            System.err.println("CLI parameter expected");
            System.err.println("java Consumer <consumer URL>");
            System.exit(1);
        }

        // Consumer
        Consumer consumer = new Consumer(new URL(args[0]));

        // Receive events
        consumer.receive_events();
    }
}
