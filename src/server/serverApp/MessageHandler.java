package server.serverApp;


import models.Channel;
import models.Message;
import models.User;

import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageHandler implements Runnable {

   private LinkedBlockingQueue<Message> messages;

   public MessageHandler(LinkedBlockingQueue<Message> messages) {
      this.messages = messages;
   }

   @Override
   public void run() {
      // TODO: 2019-02-14 change true to isrunning
      while (true) {
         if (messages.size() > 0) {
            processMessages();
         }
         try {
            Thread.sleep(10);
         } catch (InterruptedException e) {
         }

      }
   }

   private void processMessages() {
      Message m = this.messages.remove();
      switch (m.TYPE) {
         case DISCONNECT:
            sendToChannel(m);
            break;
         case CHANNEL_MESSAGE:
            sendToChannel(m);
            break;
         case JOIN_CHANNEL:
            addUserToChannel(m);
            break;
         case LEAVE_CHANNEL:
            removeUserFromChannel(m);
            break;
         case WHISPER_MESSAGE:
            sendToUser(m);
      }
   }

   private void sendToChannel(Message m) {
      if (m.CHANNEL == null || m.CHANNEL.equals("")) {
         return;
      }
      System.out.println("Sending message to channel " + m.CHANNEL);
      Channel channel = ActiveChannelController.getInstance().getChannel(m.CHANNEL);
      if (channel != null) {
         SortedSet<User> users = channel.getUsers();
         if (users != null) {
            users.forEach(u -> {
               try {
                  ActiveUserController.getInstance().getUserOutbox(u).add(m);
               } catch (Exception e) {
               }
            });
         }
      }
   }

   private void addUserToChannel(Message m) {
      System.out.println("Adding User " + m.SENDER + " to channel " + m.CHANNEL);
      String channel = m.CHANNEL;
      UUID userID = m.SENDER;
      if (channel != null && userID != null) {
         User u = ActiveUserController.getInstance().getUser(userID);
         if (u != null) {
            ActiveChannelController.getInstance().addUserToChannel(u, channel);
//            ActiveUserController.getInstance().getUserOutbox(u).add(ActiveChannelController.getInstance().getChannel(channel));
            sendToChannel(m);
         }
      }
      System.out.println("Users connected to " + m.CHANNEL + ": " + ActiveChannelController.getInstance().getChannel(m.CHANNEL).getUsers().size());
   }

   private void removeUserFromChannel(Message m) {
      if (m.SENDER != null && m.CHANNEL != null && !m.CHANNEL.equals("")) {
         this.sendToChannel(m);
         System.out.println(ActiveChannelController.getInstance().removeUserFromChannel(m.SENDER, m.CHANNEL) == true ? m.SENDER + " removed from channel " : "");
         System.out.println("Users connected to " + m.CHANNEL + ": " + ActiveChannelController.getInstance().getChannel(m.CHANNEL).getUsers().size());
      }
   }

   private void sendToUser(Message m) {
      System.out.println("Sending whisper message to " + m.RECEIVER);
      LinkedBlockingDeque<Message> outbox = ActiveUserController.getInstance().getUserOutbox(m.RECEIVER);
      outbox.add(m);
   }


}
