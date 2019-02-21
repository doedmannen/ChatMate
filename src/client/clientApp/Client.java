package client.clientApp;

import client.Main;
import client.clientApp.network.Receiver;
import client.clientApp.network.Sender;
import javafx.scene.control.Label;
import models.Message;
import models.MessageType;
import models.User;

import java.io.IOException;
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
   private ConcurrentHashMap<String, ArrayList<Label>> channelMessages;
   private String currentChannel;
   private User thisUser;

   private Client() {
      channelList = new ConcurrentSkipListMap<>();
      currentChannel = "General";
      channelMessages = new ConcurrentHashMap<>();

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

      joinChannel(currentChannel);

   }

   public void changeTitle() {
      Main.primaryStage.setTitle("Chatter Matter - Channel: " + Client.getInstance().getCurrentChannel() + " | Username: " + Client.getInstance().getThisUser().getNickName());

   }

   private void joinChannel(String channelName) {
      Message m = new Message(MessageType.JOIN_CHANNEL);
      m.CHANNEL = channelName;
      sender.sendToServer(m);
   }

   public void kill() {
      isRunning = false;
      try {
         socket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public User getThisUser() {
      return thisUser;
   }

   public void setThisUser(User thisUser) {
      this.thisUser = thisUser;
   }

   public ConcurrentHashMap<String, ArrayList<Label>> getChannelMessages() {
      return channelMessages;
   }

   public void setChannelMessages(ConcurrentHashMap<String, ArrayList<Label>> channelMessages) {
      this.channelMessages = channelMessages;
   }

   public String getCurrentChannel() {
      return currentChannel;
   }

   public void setCurrentChannel(String currentChannel) {
      this.currentChannel = currentChannel;
   }

   public boolean isRunning() {
      return this.isRunning;
   }

   public void setIsRunning(boolean isRunning) {
      this.isRunning = isRunning;
   }
}
