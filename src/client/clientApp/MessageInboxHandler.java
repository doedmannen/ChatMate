package client.clientApp;

import client.clientApp.controllers.MainGUIController;
import client.Main;
import javafx.application.Platform;
import javafx.scene.control.Label;
import models.*;

import java.util.ArrayList;

public class MessageInboxHandler {
   private static MessageInboxHandler ourInstance = new MessageInboxHandler();

   public static MessageInboxHandler getInstance() {
      return ourInstance;
   }

   private MainGUIController mainGUIController;
   private MessageCreator messageCreator;

   private MessageInboxHandler() {
      mainGUIController = (MainGUIController) Main.primaryStage.getUserData();
      messageCreator = new MessageCreator();
   }

   public void messageSwitch(Message message) {
      Platform.runLater(() -> {
         switch (message.TYPE) {
            case CHANNEL_MESSAGE:
//               Client.getInstance().getChannelMessages().get(message.CHANNEL).add(message);
               process_CHANNEL_MESSAGE(message);
               break;
            case JOIN_CHANNEL:
               process_JOIN_MESSAGE(message);
               Client.getInstance().channelList.get(message.CHANNEL).add(new User(message.NICKNAME, message.SENDER));
               System.out.println(mainGUIController);
               mainGUIController.refreshUserList();
//                mainGUIController.getChatBox().getChildren().add(messageCreator.joinChannelMessage(message));
               break;
            case LEAVE_CHANNEL:
               process_LEAVE_MESSAGE(message);
               User u = Client.getInstance().channelList.get(message.CHANNEL)
                       .stream()
                       .filter(user -> user.getID() == message.SENDER)
                       .toArray(User[]::new)[0];
               Client.getInstance().channelList.get(message.CHANNEL).remove(u);
               mainGUIController.refreshUserList();
//                mainGUIController.getChatBox().getChildren().add(messageCreator.leaveChannelMessage(message));
               break;
            case DISCONNECT:
               process_DISCONNECT_MESSAGE(message);
               User disconnect = Client.getInstance().channelList.get(message.CHANNEL)
                       .stream()
                       .filter(user -> user.getID() == message.SENDER)
                       .toArray(User[]::new)[0];
               Client.getInstance().channelList.get(message.CHANNEL).remove(disconnect);
               mainGUIController.refreshUserList();
//                mainGUIController.getChatBox().getChildren().add(messageCreator.disconnectMessage(message));
               break;
            case NICKNAME_CHANGE:
               process_NICKNAME_CHANGE(message);
               if (message.SENDER == Client.getInstance().getThisUser().getID()) {
                  Client.getInstance().getThisUser().setNickName(message.TEXT_CONTENT);
                  Client.getInstance().changeTitle();
               }
               Client.getInstance().channelList.get(message.CHANNEL).forEach(user -> {
                  if (user.getID() == message.SENDER) {
                     user.setNickName(message.TEXT_CONTENT);
                  }
               });
//                mainGUIController.getChatBox().getChildren().add(messageCreator.nicknameMessage(message));
               mainGUIController.refreshUserList();
               break;
            case WHISPER_MESSAGE:
               mainGUIController.getChatBox().getChildren().add(messageCreator.whisperMessage(message));
               break;
            case CONNECT:
               Client.getInstance().setThisUser(new User(message.NICKNAME, message.RECEIVER));
               Main.primaryStage.setTitle("Chatter Matter - " + message.NICKNAME);
               break;
            case ERROR:
               break;
            case WARNING:
//                mainGUIController.getChatBox().getChildren().add(messageCreator.warningMessage(message));
               break;
         }
      });
   }

   public void process_CHANNELL(Channel channel) {
      ArrayList<ChatLabel> list = Client.getInstance().getChannelMessages().getOrDefault(channel.getName(), new ArrayList<>());
      Client.getInstance().getChannelMessages().put(channel.getName(), list);
      Platform.runLater(() -> {
         mainGUIController.channels.add(channel);
      });
   }

   public void process_NICKNAME_CHANGE(Message message) {
      ChatLabel label = messageCreator.nicknameMessage(message);
      Client.getInstance().getChannelMessages().get(message.CHANNEL).add(label);
      if (message.CHANNEL.equals(Client.getInstance().getCurrentChannel())) {
         mainGUIController.getChatBox().getChildren().add(label);
      }
   }

   public void process_CHANNEL_MESSAGE(Message message) {
      ChatLabel label = messageCreator.channelMessage(message);
      Client.getInstance().getChannelMessages().get(message.CHANNEL).add(label);
      if (message.CHANNEL.equals(Client.getInstance().getCurrentChannel())) {
         mainGUIController.getChatBox().getChildren().add(label);
      }
   }

   public void process_JOIN_MESSAGE(Message message) {
      ChatLabel label = messageCreator.joinChannelMessage(message);
      Client.getInstance().getChannelMessages().get(message.CHANNEL).add(label);
      if (message.CHANNEL.equals(Client.getInstance().getCurrentChannel())) {
         mainGUIController.getChatBox().getChildren().add(label);
      }
   }

   public void process_LEAVE_MESSAGE(Message message) {
      ChatLabel label = messageCreator.leaveChannelMessage(message);
      Client.getInstance().getChannelMessages().get(message.CHANNEL).add(label);
      if (message.CHANNEL.equals(Client.getInstance().getCurrentChannel())) {
         mainGUIController.getChatBox().getChildren().add(label);
      }
   }

   public void process_DISCONNECT_MESSAGE(Message message) {
      ChatLabel label = messageCreator.disconnectMessage(message);
      Client.getInstance().getChannelMessages().get(message.CHANNEL).add(label);
      if (message.CHANNEL.equals(Client.getInstance().getCurrentChannel())) {
         mainGUIController.getChatBox().getChildren().add(label);
      }
   }

   public void printUsers() {
      Platform.runLater(() -> mainGUIController.printUsers());
   }

}
