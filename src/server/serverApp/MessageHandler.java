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
   private String[] badWordList;
   private String[] betterWordList;
   public MessageHandler(LinkedBlockingQueue<Sendable> messages) {
      this.messages = messages;
      badWordList = new String[]
              {"fuck", "pussy", "cunt", "whore", "nigger", "ass", "bitch", "cock", "poop", "shit", "fag", "dick", "slut"};
      betterWordList = new String[]
              {"flower", "potato", "tomato", "love", "kitten"};
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
      fixMessageContent(m);
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

   private void fixMessageContent(Message m){
      // Only apply is there is text-content and the message is sent to a channel or user
      if(m.TEXT_CONTENT != null && m.TYPE == MessageType.CHANNEL_MESSAGE || m.TYPE == MessageType.WHISPER_MESSAGE){
         // remove multiple whitespaces and trim
         m.TEXT_CONTENT = m.TEXT_CONTENT.replaceAll("[ ]{2,}", " ").trim();
         // Limit length if client sent us to much text
         m.TEXT_CONTENT = m.TEXT_CONTENT.length() > 1000 ? m.TEXT_CONTENT.substring(0,1000) : m.TEXT_CONTENT;
         applyWordFilter(m);
      }
   }

   private void applyWordFilter(Message m){
      m.TEXT_CONTENT = m.TEXT_CONTENT
              .replaceAll("(("+String.join("|", badWordList)+")\\w*\\b)",
                      betterWordList[(int) (Math.random() * betterWordList.length)]);
   }

   private boolean checkIfChannelIsValid(String channel){
      return channel.matches("^[^\\s]{3,10}$");
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

   private void changeUserNickName(Message m) {
      // Trim the string
      m.TEXT_CONTENT = m.TEXT_CONTENT.trim();
      if (validUserNickName(m.TEXT_CONTENT)) {
         // If the username is valid, change it on the server
         User user = ActiveUserController.getInstance().getUser(m.SENDER);
         user.setNickName(m.TEXT_CONTENT);
         // Send new username to all channels the user is active in

         sendOutNewUserNickName(user, m);
      } else {
         // If username was invalid, send an error to the user
         sendErrorToUser(m.SENDER, m.CHANNEL, "The username you wanted is not valid. " +
                 "\nPlease choose one with no whitespaces and 3-10 characters in length.");
      }
   }

   private void sendErrorToUser(UUID user_ID, String channel, String errorText) {
      if(user_ID != null && channel != null){
         Message errorMessage = new Message(MessageType.ERROR);
         errorMessage.TEXT_CONTENT = errorText;
         errorMessage.RECEIVER = user_ID;
         errorMessage.CHANNEL = channel;
         sendToUser(errorMessage);
      }
   }

   private void sendOutNewUserNickName(User user, Message m) {
      String[] userChannels = ActiveChannelController.getInstance().getChannelsForUser(user);

      if (userChannels.length > 0) {
         Arrays.stream(userChannels).forEach(channel -> {
            System.out.println("Sending to " + channel);
            Message messageToBeSent = new Message(MessageType.NICKNAME_CHANGE);
            messageToBeSent.TEXT_CONTENT = m.TEXT_CONTENT;
            messageToBeSent.SENDER = m.SENDER;
            messageToBeSent.NICKNAME = m.NICKNAME;
            messageToBeSent.CHANNEL = channel;
            sendToChannel(messageToBeSent);
         });
      } else {
         Message messageToBeSent = new Message(MessageType.NICKNAME_CHANGE);
         messageToBeSent.TEXT_CONTENT = m.TEXT_CONTENT;
         messageToBeSent.SENDER = m.SENDER;
         messageToBeSent.NICKNAME = m.NICKNAME;
         messageToBeSent.RECEIVER = m.SENDER;
         sendToUser(messageToBeSent);
      }
   }


   private boolean validUserNickName(String newName) {
      return newName.matches("^[^\\s]{3,10}$");
   }

   private void addUserToChannel(Message m) {
      String channel = m.CHANNEL;
      User user = ActiveUserController.getInstance().getUser(m.SENDER);
      boolean userIsInChannel = ActiveChannelController.getInstance().userIsInChannel(channel, user);
      if (channel != null && user != null && !userIsInChannel && checkIfChannelIsValid(m.CHANNEL)) {
            m.NICKNAME = user.getNickName();
            ActiveChannelController.getInstance().addUserToChannel(user, channel);
            Channel c = ActiveChannelController.getInstance().getChannel(m.CHANNEL);
            ActiveUserController.getInstance().getUserOutbox(user).add(c);
            sendToChannel(m);
      }
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
