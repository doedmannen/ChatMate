package server.serverApp;


import models.*;

import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static models.MessageType.*;

public class MessageHandler implements Runnable {

   private LinkedBlockingQueue<Sendable> messages;

   public MessageHandler(LinkedBlockingQueue<Sendable> messages) {
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
            Thread.sleep(1);
         } catch (InterruptedException e) {
         }

      }
   }

   private void processMessages() {
      Message m = (Message) this.messages.remove();
      switch (m.getType()) {
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
      if (m.getChannel() == null || m.getChannel().equals("")) {
         return;
      }
      System.out.println("Sending message to channel " + m.getChannel());
      Channel channel = ActiveChannelController.getInstance().getChannel(m.getChannel());
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
      Message message = new Message.MessageBuilder(MessageType.JOIN_CHANNEL)
              .fromSender(m.getSender())
              .toChannel(m.getChannel())
              .nickname(ActiveUserController.getInstance().getUser(m.getSender()).getNickName())
              .build();
      System.out.println("Adding User " + m.getSender() + " to channel " + m.getChannel());
      String channel = m.getChannel();
      UUID userID = m.getSender();
      if (channel != null && userID != null) {
         User u = ActiveUserController.getInstance().getUser(userID);
         if (u != null) {
            ActiveChannelController.getInstance().addUserToChannel(u, channel);
//            ActiveUserController.getInstance().getUserOutbox(u).add(ActiveChannelController.getInstance().getChannel(channel));
            sendToChannel(message);
         }
      }
      System.out.println("Users connected to " + m.getChannel() + ": " + ActiveChannelController.getInstance().getChannel(m.getChannel()).getUsers().size());
   }

   private void removeUserFromChannel(Message m) {
      if (m.getSender() != null && m.getChannel() != null && !m.getChannel().equals("")) {
         this.sendToChannel(m);
         System.out.println(ActiveChannelController.getInstance().removeUserFromChannel(m.getSender(), m.getChannel()) == true ? m.getSender() + " removed from channel " : "");
         System.out.println("Users connected to " + m.getChannel() + ": " + ActiveChannelController.getInstance().getChannel(m.getChannel()).getUsers().size());
      }
   }

   private void sendToUser(Message m) {
      System.out.println("Sending whisper message to " + m.getReceiver());
      LinkedBlockingDeque<Sendable> outbox = ActiveUserController.getInstance().getUserOutbox(m.getReceiver());
      outbox.add(m);
   }


}
