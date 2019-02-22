package client.clientApp;

import client.Controller;
import client.Main;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import models.SerializableLabel;
import models.Message;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MessageCreator {
   Controller controller;

   public MessageCreator() {
      controller = (client.Controller) Main.primaryStage.getUserData();
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
   public SerializableLabel createLabel(Message message) {
      SerializableLabel label = null;
      switch (message.TYPE) {
         case DISCONNECT:
            label = labelCreator(message.TIMESTAMP + message.NICKNAME + " has disconnected.", Color.DARKSLATEGRAY, "leave_channel");
            break;
         case CHANNEL_MESSAGE:
            label = labelCreator(message.TIMESTAMP + message.NICKNAME + ": " + message.TEXT_CONTENT, Color.BLACK, "channel_message");
            break;
         case JOIN_CHANNEL:
            label = labelCreator(message.TIMESTAMP + message.NICKNAME + " has joined the channel.", Color.GREEN, "join_channel");
            break;
         case LEAVE_CHANNEL:
            label = labelCreator(message.TIMESTAMP + message.NICKNAME + " has left the channel.", Color.RED, "leave_channel");
            break;
         case WHISPER_MESSAGE:
            label = labelCreator(message.TIMESTAMP + message.NICKNAME + " whispers: " + message.TEXT_CONTENT, Color.PURPLE, "channel_message");
            break;
         case NICKNAME_CHANGE:
            label = labelCreator(message.TIMESTAMP + message.NICKNAME + " is now " + message.TEXT_CONTENT + ".", Color.DARKMAGENTA, "leave_channel");
            break;
         case ERROR:
            label = labelCreator("ERROR: " + message.TEXT_CONTENT, Color.ORANGERED, "error");
            break;
         case WARNING:
            label = labelCreator("Warning: " + message.TEXT_CONTENT, Color.ORANGERED, "warning");
            break;
      }
      return label;
   }
}
