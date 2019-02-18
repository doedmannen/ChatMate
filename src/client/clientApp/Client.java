package client.clientApp;

import models.Message;
import models.MessageType;
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
    private String nickname;


    private Client() {
        channelList = new ConcurrentSkipListMap<>();
        currentChannel = "General";
        channelList.put("Generalhelvete", new ConcurrentSkipListSet<>());
        channelMessages = new ConcurrentHashMap<>();

        //temp test
        channelMessages.put("General", new ArrayList<Message>());
        setNickname("Boris");
        currentChannel = "General";
//        ConcurrentSkipListSet<User> c = new ConcurrentSkipListSet<>();
//        c.add(new User("Failip"));
//        c.add(new User("Ted"));
//        c.add(new User("Anton"));
//        channelList.put("General",c);

        try {
            socket = new Socket("10.155.88.109", 54323);
            isRunning = true;
            sender = new Sender(socket);
            reciever = new Receiver(socket);
            sender.start();
            reciever.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Message m = new Message.MessageBuilder(MessageType.JOIN_CHANNEL)
                .toChannel("General")
                .build();
        sender.sendToServer(m);

    }
    public void kill() {
        isRunning=false;

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
