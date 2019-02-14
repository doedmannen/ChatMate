package client.clientApp;

import models.User;

import java.net.Socket;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Client {
    private static Client ourInstance = new Client();
    ///todo Make private
    public static Client getInstance() {
        return ourInstance;
    }
    boolean isRunning;
    Socket socket;
    public Sender sender;
    public Receiver reciever;
    public ConcurrentSkipListMap<String, ConcurrentSkipListSet<User>> channelList;
    private String currentChannel;


    private Client() {
        channelList = new ConcurrentSkipListMap<>();
        currentChannel = "General";

        ConcurrentSkipListSet<User> c = new ConcurrentSkipListSet<>();
        c.add(new User("Failip"));
        c.add(new User("Ted"));
        c.add(new User("Anton"));
        channelList.put("General",c);

        try {
            socket = new Socket("10.155.88.94", 54322);
            isRunning = true;
            sender = new Sender(socket);
            reciever = new Receiver(socket);
            sender.start();
            reciever.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void kill() {
        isRunning=false;

    }

    public String getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }
}
