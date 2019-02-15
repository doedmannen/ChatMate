package client.clientApp;

import models.Message;

import java.net.Socket;

public class TEMP_CLIENT {
    /*
     * TODO TESTING ONLY, REMOVE THIS SHIT LATER :/
     *
     * */
    static boolean isRunning;
    static Socket socket;
    static Sender sender;
    static Reciever reciever;


    public static void main(String[] args) {


        try {
            socket = new Socket("10.155.88.109", 54321);
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
