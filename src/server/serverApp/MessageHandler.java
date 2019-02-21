package server.serverApp;


import models.*;

import javax.swing.*;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

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
            break;
         case NICKNAME_CHANGE:
            changeUserNickName(m);
            break;
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

   private void changeUserNickName(Message m){
      // Trim the string
      m.TEXT_CONTENT = m.TEXT_CONTENT.trim();
      if(validUserNickName(m.TEXT_CONTENT)){
         // If the username is valid, change it on the server
         User user = ActiveUserController.getInstance().getUser(m.SENDER);
         user.setNickName(m.TEXT_CONTENT);
         // Send new username to all channels the user is active in
         sendOutNewUserNickName(user, m);
      }else {
         // If username was invalid, send an error to the user
         sendErrorInvalidUserNickName(m.SENDER, m.CHANNEL);
      }
   }

   private void sendErrorInvalidUserNickName(UUID user_ID, String channel){
      Message errorMessage = new Message(MessageType.ERROR);
      errorMessage.TEXT_CONTENT = "The username you wanted is not valid. \nPlease choose one with no whitespaces and 3-20 in length.";
      errorMessage.RECEIVER = user_ID;
      errorMessage.CHANNEL = channel;
      sendToUser(errorMessage);
   }

   private void sendOutNewUserNickName(User user, Message m){
      String[] userChannels = ActiveChannelController.getInstance().getChannelsForUser(user);
      Arrays.stream(userChannels).forEach(channel -> {
         System.out.println("Sending to " + channel);
         Message messageToBeSent = new Message(MessageType.NICKNAME_CHANGE);
         messageToBeSent.TEXT_CONTENT = m.TEXT_CONTENT;
         messageToBeSent.SENDER = m.SENDER;
         messageToBeSent.NICKNAME = m.NICKNAME;
         messageToBeSent.CHANNEL = channel;
         sendToChannel(messageToBeSent);
      });
   }

   private boolean validUserNickName(String newName){
      return newName.matches("^[^\\s]{3,20}$");
   }

   private void addUserToChannel(Message m) {
      System.out.println("Adding User " + m.SENDER + " to channel " + m.CHANNEL);
      String channel = m.CHANNEL;
      User user = ActiveUserController.getInstance().getUser(m.SENDER);
      if (channel != null && user != null) {
         m.NICKNAME = user.getNickName();
         ActiveChannelController.getInstance().addUserToChannel(user, channel);
         Channel c = ActiveChannelController.getInstance().getChannel(m.CHANNEL);
         ActiveUserController.getInstance().getUserOutbox(user).add(c);
//         resendChannelToClients(m.CHANNEL);
         sendToChannel(m);
      }
      System.out.println("Users connected to " + m.CHANNEL + ": " + ActiveChannelController.getInstance().getChannel(m.CHANNEL).getUsers().size());
   }

   private void removeUserFromChannel(Message m) {
      if (m.SENDER != null && m.CHANNEL != null && !m.CHANNEL.equals("")) {
         this.sendToChannel(m);
         System.out.println(ActiveChannelController.getInstance().removeUserFromChannel(m.SENDER, m.CHANNEL) == true ? m.SENDER + " removed from channel " : "");
      }
   }

   private void sendToUser(Message m) {
      System.out.println("Sending whisper message to " + m.RECEIVER);
      ActiveUserController.getInstance().getUserOutbox(m.RECEIVER).add(m);
   }


}
