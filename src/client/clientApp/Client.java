package client.clientApp;

import models.User;

import java.net.Socket;
import java.util.concurrent.ConcurrentSkipListSet;

public class Client {
    private static Client ourInstance = new Client();

    public static Client getInstance() {
        return ourInstance;
    }
    boolean isRunning;
    Socket socket;
    public Sender sender;
    public Reciever reciever;
    public ConcurrentSkipListSet<User> userList;
    private Client() {
        userList = new ConcurrentSkipListSet<User>();

        userList.add(new User("Failip"));
        userList.add(new User("Ted"));
        userList.add(new User("Anton"));
        System.out.println(userList.first().getNickName());

        try {
            socket = new Socket("10.155.88.94", 54322);
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
