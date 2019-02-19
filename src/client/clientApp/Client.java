package client.clientApp;

import models.Message;
import models.MessageType;
import models.User;

import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;
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
    private User thisUser;

    private Client() {
        channelList = new ConcurrentSkipListMap<>();
        currentChannel = "General";
        channelMessages = new ConcurrentHashMap<>();
        channelMessages.put("General", new ArrayList<Message>());

        try {
            socket = new Socket("10.155.88.55", 54322);
            isRunning = true;
            sender = new Sender(socket);
            reciever = new Receiver(socket);
            sender.start();
            reciever.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        joinChannel(currentChannel);

    }

    private void joinChannel(String channelName){
        Message m = new Message(MessageType.JOIN_CHANNEL);
        m.CHANNEL = channelName;
        sender.sendToServer(m);
    }

    public void kill() {
        isRunning=false;

    }

    public User getThisUser() {
        return thisUser;
    }

    public void setThisUser(User thisUser) {
        this.thisUser = thisUser;
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
