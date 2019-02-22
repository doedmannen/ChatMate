package client.clientApp;

import client.Controller;
import client.Main;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import models.SerializableLabel;
import models.Message;


public class MessageCreator {
   Controller controller;

   public MessageCreator() {
      controller = (Controller) Main.primaryStage.getUserData();
   }

   public SerializableLabel labelCreator(String text, Paint color, String id) {
      SerializableLabel label = new SerializableLabel();
      label.setText(text);
      label.setTextFill(color);
      label.setId(id);
      label.setWrapText(true);
      label.save();
      return label;
   }

   @FXML
   public SerializableLabel channelMessage(Message message) {
      return labelCreator(message.NICKNAME + ": " + message.TEXT_CONTENT, Color.BLACK, "channel_message");
//        controller.getChatBox().getChildren().add(channelMessage);
   }

   @FXML
   public SerializableLabel whisperMessage(Message message) {
      return labelCreator(message.NICKNAME + " whispers: " + message.TEXT_CONTENT, Color.PURPLE, "channel_message");
//      controller.getChatBox().getChildren().add(whisperMessage);
   }

   @FXML
   public SerializableLabel warningMessage(Message message) {
      return labelCreator("Warning: " + message.TEXT_CONTENT, Color.RED, "warning");
//      controller.getChatBox().getChildren().add(warningLabel);
   }

   @FXML
   public SerializableLabel joinChannelMessage(Message message) {
      return labelCreator(message.NICKNAME + " has joined the channel.", Color.GREEN, "join_channel");
//      controller.getChatBox().getChildren().add(joinMessage);
   }

   @FXML
   public SerializableLabel leaveChannelMessage(Message message) {
      return labelCreator(message.NICKNAME + " has left the channel.", Color.RED, "leave_channel");
//      controller.getChatBox().getChildren().add(leaveMessage);
   }

   @FXML
   public SerializableLabel disconnectMessage(Message message) {
      return labelCreator(message.NICKNAME + " has disconnected.", Color.DARKSLATEGRAY, "leave_channel");
//       controller.getChatBox().getChildren().add(disconnectMessage);
   }

   @FXML
   public SerializableLabel nicknameMessage(Message message) {
      return labelCreator(message.NICKNAME + " is now " + message.TEXT_CONTENT + ".", Color.DARKMAGENTA, "leave_channel");
//        controller.getChatBox().getChildren().add(nicknameMessage);
   }

}
