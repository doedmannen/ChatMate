package client.clientApp;

import java.net.Socket;

public class Client {
    static boolean isRunning;
    Socket socket;
    public static Sender sender;
    Reciever reciever;
    public Client() {
        try {
            socket = new Socket("10.155.90.30", 54321);
            isRunning = true;
            sender = new Sender(socket);
            reciever = new Reciever(socket);
            sender.start();
            reciever.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void kill() {
        isRunning=false;

    }
}
