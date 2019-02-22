package client.clientApp;

import client.Controller;
import client.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Message;


public class MessageCreator {
    Controller controller;

    public MessageCreator() {
        controller = (client.Controller) Main.primaryStage.getUserData();
    }

    public Label labelCreator(String text, String id) {
        Label label = new Label();
        label.setText(text);
        label.setId(id);
        label.setWrapText(true);
        return label;
    }

    @FXML
    public Label createLabel(Message message) {
        Label label = null;
        switch (message.TYPE) {
            case DISCONNECT:
                label = labelCreator(message.NICKNAME + " has disconnected.", "disconnect_message");
            break;
            case CHANNEL_MESSAGE:
                label = labelCreator(message.NICKNAME + ": " + message.TEXT_CONTENT, "channel_message");
                break;
            case JOIN_CHANNEL:
                label = labelCreator(message.NICKNAME + " has joined the channel.", "join_message");
                break;
            case LEAVE_CHANNEL:
                label = labelCreator(message.NICKNAME + " has left the channel.", "leave_channel");
                break;
            case WHISPER_MESSAGE:
                label = labelCreator(message.NICKNAME + " whispers: " + message.TEXT_CONTENT, "whisper_message");
                break;
            case NICKNAME_CHANGE:
                label = labelCreator(message.NICKNAME + " is now " + message.TEXT_CONTENT + ".", "nickname_message");
            break;
            case ERROR:
                label = labelCreator("ERROR: " + message.TEXT_CONTENT, "error");
                break;
            case WARNING:
                label = labelCreator("Warning: " + message.TEXT_CONTENT, "warning");
                break;
        }
        return label;
    }
}
