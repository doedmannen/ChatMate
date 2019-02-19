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
    public void channelMessage(Message message) {
        Label channelMessage = labelCreator(message.NICKNAME + ": " + message.TEXT_CONTENT, Color.BLACK, "channel_message");
        controller.getChatBox().getChildren().add(channelMessage);
    }

    @FXML
    public void whisperMessage(Message message) {
        Label whisperMessage = labelCreator(message.NICKNAME + " whispers: " + message.TEXT_CONTENT, Color.PURPLE, "channel_message");
        controller.getChatBox().getChildren().add(whisperMessage);
    }

    @FXML
    public void warningMessage(Message message) {
        Label warningLabel = labelCreator("Warning: " + message.TEXT_CONTENT, Color.RED, "warning");
        controller.getChatBox().getChildren().add(warningLabel);
    }

    @FXML
    public void joinChannelMessage(Message message) {
        Label joinMessage = labelCreator(message.NICKNAME + " has joined the channel.", Color.GREEN, "join_channel");
        controller.getChatBox().getChildren().add(joinMessage);
    }

    @FXML
    public void leaveChannelMessage(Message message) {
        Label leaveMessage = labelCreator(message.NICKNAME + " has left the channel.", Color.LAVENDER, "leave_channel");
        controller.getChatBox().getChildren().add(leaveMessage);
    }

    @FXML
    public void disconnectMessage(Message message) {
        Label disconnectMessage = labelCreator(message.NICKNAME + " has disconnected.", Color.DARKSLATEGRAY, "leave_channel");
        controller.getChatBox().getChildren().add(disconnectMessage);
    }

    @FXML
    public void nicknameMessage(Message message) {
        Label nicknameMessage = labelCreator(message.NICKNAME + " is now " + message.TEXT_CONTENT + ".", Color.DARKMAGENTA, "leave_channel");
        controller.getChatBox().getChildren().add(nicknameMessage);
    }

}
