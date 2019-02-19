package client.clientApp;

import client.Controller;
import client.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import models.Message;
import models.MessageType;


public class MessageCreator {
   Controller controller;

   public MessageCreator() {
      controller = (client.Controller) Main.primaryStage.getUserData();
   }

   public Label labelCreator(String text, Paint color, String id) {
      Label label = new Label();
      label.setText(text);
      label.setTextFill(color);
      label.setId(id);
      label.setWrapText(true);
      return label;
   }

   @FXML
   public Label channelMessage(Message message) {
      return labelCreator(message.NICKNAME + ": " + message.TEXT_CONTENT, Color.BLACK, "channel_message");
//        controller.getChatBox().getChildren().add(channelMessage);
   }

   @FXML
   public Label whisperMessage(Message message) {
      return labelCreator(message.NICKNAME + " whispers: " + message.TEXT_CONTENT, Color.PURPLE, "channel_message");
//      controller.getChatBox().getChildren().add(whisperMessage);
   }

   @FXML
   public Label warningMessage(Message message) {
      return labelCreator("Warning: " + message.TEXT_CONTENT, Color.RED, "warning");
//      controller.getChatBox().getChildren().add(warningLabel);
   }

   @FXML
   public Label joinChannelMessage(Message message) {
      return labelCreator(message.NICKNAME + " has joined the channel.", Color.GREEN, "join_channel");
//      controller.getChatBox().getChildren().add(joinMessage);
   }

   @FXML
   public Label leaveChannelMessage(Message message) {
      return labelCreator(message.NICKNAME + " has left the channel.", Color.LAVENDER, "leave_channel");
//      controller.getChatBox().getChildren().add(leaveMessage);
   }

   @FXML
   public Label disconnectMessage(Message message) {
      return labelCreator(message.NICKNAME + " has disconnected.", Color.DARKSLATEGRAY, "leave_channel");
//       controller.getChatBox().getChildren().add(disconnectMessage);
   }

    @FXML
    public Label nicknameMessage(Message message) {
        return labelCreator(message.NICKNAME + " is now " + message.TEXT_CONTENT + ".", Color.DARKMAGENTA, "leave_channel");
//        controller.getChatBox().getChildren().add(nicknameMessage);
    }

}
