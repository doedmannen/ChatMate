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
    public Label createLabel(Message message) {
        Label label = null;
        switch (message.TYPE) {
            case DISCONNECT:
                label = labelCreator(message.NICKNAME + " has disconnected.", Color.DARKSLATEGRAY, "leave_channel");
            break;
            case CHANNEL_MESSAGE:
                label = labelCreator(message.NICKNAME + ": " + message.TEXT_CONTENT, Color.BLACK, "channel_message");
                break;
            case JOIN_CHANNEL:
                label = labelCreator(message.NICKNAME + " has joined the channel.", Color.GREEN, "join_channel");
                break;
            case LEAVE_CHANNEL:
                label = labelCreator(message.NICKNAME + " has left the channel.", Color.RED, "leave_channel");
                break;
            case WHISPER_MESSAGE:
                label = labelCreator(message.NICKNAME + " whispers: " + message.TEXT_CONTENT, Color.PURPLE, "channel_message");
                break;
            case NICKNAME_CHANGE:
                label = labelCreator(message.NICKNAME + " is now " + message.TEXT_CONTENT + ".", Color.DARKMAGENTA, "leave_channel");
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
