package client.clientApp;

import client.Controller;
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

   private Controller controller;
   private MessageCreator messageCreator;

   private MessageInboxHandler() {
      controller = (client.Controller) Main.primaryStage.getUserData();
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
               Client.getInstance().channelList.get(message.CHANNEL).add(new User(message.NICKNAME, message.SENDER));
               controller.refreshUserList();
                controller.getChatBox().getChildren().add(messageCreator.joinChannelMessage(message));
               break;
            case LEAVE_CHANNEL:
               User u = Client.getInstance().channelList.get(message.CHANNEL)
                       .stream()
                       .filter(user -> user.getID() == message.SENDER)
                       .toArray(User[]::new)[0];
               Client.getInstance().channelList.get(message.CHANNEL).remove(u);
               controller.refreshUserList();
                controller.getChatBox().getChildren().add(messageCreator.leaveChannelMessage(message));
               break;
            case DISCONNECT:
               User disconnect = Client.getInstance().channelList.get(message.CHANNEL)
                       .stream()
                       .filter(user -> user.getID() == message.SENDER)
                       .toArray(User[]::new)[0];
               Client.getInstance().channelList.get(message.CHANNEL).remove(disconnect);
               controller.refreshUserList();
                controller.getChatBox().getChildren().add(messageCreator.disconnectMessage(message));
               break;
            case NICKNAME_CHANGE:
               if (message.SENDER == Client.getInstance().getThisUser().getID()) {
                        Client.getInstance().getThisUser().setNickName(message.TEXT_CONTENT);
                    }
                    Client.getInstance().channelList.get(message.CHANNEL).forEach(user -> {
                        if (user.getID() == message.SENDER) {
                            user.setNickName(message.TEXT_CONTENT);
                        }
                    });
                controller.getChatBox().getChildren().add(messageCreator.nicknameMessage(message));
                    controller.refreshUserList();
                    break;
            case WHISPER_MESSAGE:
                controller.getChatBox().getChildren().add(messageCreator.whisperMessage(message));
               break;
            case CONNECT:
               Client.getInstance().setThisUser(new User(message.NICKNAME, message.RECEIVER));
               Main.primaryStage.setTitle("Chatter Matter - " + message.NICKNAME);
               break;
            case ERROR:
               break;
            case WARNING:
                controller.getChatBox().getChildren().add(messageCreator.warningMessage(message));
               break;
         }
      });
   }

   public void process_CHANNELL(Channel channel) {
      ArrayList<Label> list = Client.getInstance().getChannelMessages().getOrDefault(channel.getName(), new ArrayList<>());
      Client.getInstance().getChannelMessages().put(channel.getName(), list);
      Platform.runLater(() -> {
         controller.channels.add(channel);
      });
   }

   public void process_CHANNEL_MESSAGE(Message message) {
      Label label = messageCreator.channelMessage(message);
      Client.getInstance().getChannelMessages().get(message.CHANNEL).add(label);
      if (message.CHANNEL.equals(Client.getInstance().getCurrentChannel())) {
         controller.getChatBox().getChildren().add(label);
      }
   }

   public void printUsers() {
      Platform.runLater(() -> controller.printUsers());
   }

}
