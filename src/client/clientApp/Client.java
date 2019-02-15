package client.clientApp;

import models.Message;
import models.User;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
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
    private ConcurrentHashMap<String, ArrayList<Message>> channelMessages;
    private String currentChannel;


    private Client() {
        channelList = new ConcurrentSkipListMap<>();
        currentChannel = "General";
        channelList.put("General", new ConcurrentSkipListSet<>());
        channelList.put("General2", new ConcurrentSkipListSet<>());
        channelList.put("General3", new ConcurrentSkipListSet<>());
        channelMessages = new ConcurrentHashMap<>();

        //temp test
        channelMessages.put("General", new ArrayList<Message>());

//        ConcurrentSkipListSet<User> c = new ConcurrentSkipListSet<>();
//        c.add(new User("Failip"));
//        c.add(new User("Ted"));
//        c.add(new User("Anton"));
//        channelList.put("General",c);

        try {
            socket = new Socket("localhost", 54322);
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

    public ConcurrentHashMap<String, ArrayList<Message>> getChannelMessages() {
        return channelMessages;
    }

    public void setChannelMessages(ConcurrentHashMap<String, ArrayList<Message>> channelMessages) {
        this.channelMessages = channelMessages;
    }

    public String getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(String currentChannel) {
        this.currentChannel = currentChannel;
    }
}
