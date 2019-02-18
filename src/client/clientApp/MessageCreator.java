package client.clientApp;

import client.Controller;
import client.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import models.Message;


public class MessageCreator{
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
    public void channelMessage(Message message) {
        Label channelMessage = labelCreator(message.getNickname() + ": " + message.getTextContent(), Color.BLACK, "channel_message");
        controller.getChatBox().getChildren().add(channelMessage);
    }

    @FXML
    public void warningMessage(Message message) {
        Label warningLabel = labelCreator("Warning: " + message.getTextContent(), Color.RED, "warning");
        controller.getChatBox().getChildren().add(warningLabel);
    }

    @FXML
    public void joinChannelMessage(Message message) {
        Label joinMessage = labelCreator(message.getSender() +" has joined the channel.", Color.GREEN, "join_channel");
        controller.getChatBox().getChildren().add(joinMessage);
    }

}
