package client.clientApp;

import client.Controller;
import client.Main;
import models.Channel;
import models.Message;
import models.Sendable;
import models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.concurrent.ConcurrentSkipListSet;

import static client.Main.primaryStage;

public class Receiver extends Thread{
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private Controller controller = (client.Controller) primaryStage.getUserData();

    public Receiver(Socket socket){
        this.socket=socket;
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            System.out.println("failed to create input stream");
            Client.getInstance().isRunning = false;
            // todo kolla om server är död, prova återanslutning
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Receiver is running");
        while (Client.getInstance().isRunning){
            try {
                Sendable inData = (Sendable) objectInputStream.readObject();

                if (inData instanceof Message) {
                    Message message = (Message) inData;
                    System.out.println(message.TYPE + message.NICKNAME + message.CHANNEL + message.TEXT_CONTENT + message.RECEIVER + message.SENDER);
//                    MessageInboxHandler.getInstance().messageSwitch(message);
//                    controller.getOutput_text().appendText(message.TEXT_CONTENT + "\n"); //For debugging javaFX print
                } else if (inData instanceof Channel) {
                    Channel channel = (Channel) inData;
                    System.out.println("User joined " + channel.getName() + " with " + channel.getUsers().size());
//                    if (Client.getInstance().channelList.containsKey(channel.getName())) {
//                        Client.getInstance().channelList.remove(channel.getName());
//                        Client.getInstance().channelList.put(channel.getName(), (ConcurrentSkipListSet<User>) channel.getUsers());
//                    } else {
//                        Client.getInstance().channelList.put(channel.getName(), (ConcurrentSkipListSet<User>) channel.getUsers());
//                    }
//                    controller.printUsers();
                }
//                System.out.println(message);
            }catch (IOException e){
                System.out.println("Read Error");
                Client.getInstance().isRunning = false;
                // todo kolla om server är död, prova återanslutning
            }catch (ClassNotFoundException e){
                System.out.println("Message Error");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
