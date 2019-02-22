package client.clientApp;

import client.clientApp.controllers.ChatWindowController;
import client.ClientMain;
import javafx.fxml.FXML;
import models.SerializableLabel;
import models.Message;

public class MessageCreator {
    ChatWindowController chatWindowController;

    public MessageCreator() {
        chatWindowController = (ChatWindowController) ClientMain.primaryStage.getUserData();
    }

    public SerializableLabel labelCreator(String text, String id) {
        SerializableLabel label = new SerializableLabel();
        label.setText(text);
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
                label = labelCreator(message.TIMESTAMP + message.NICKNAME + " has disconnected.", "disconnect_message");
                break;
            case CHANNEL_MESSAGE:
                label = labelCreator(message.TIMESTAMP + message.NICKNAME + ": " + message.TEXT_CONTENT, "channel_message");
                break;
            case JOIN_CHANNEL:
                label = labelCreator(message.TIMESTAMP + message.NICKNAME + " has joined the channel.", "join_message");
                break;
            case LEAVE_CHANNEL:
                label = labelCreator(message.TIMESTAMP + message.NICKNAME + " has left the channel.", "leave_channel");
                break;
            case WHISPER_MESSAGE:
                label = labelCreator(message.TIMESTAMP + message.NICKNAME + " whispers: " + message.TEXT_CONTENT, "whisper_message");
                break;
            case NICKNAME_CHANGE:
                label = labelCreator(message.TIMESTAMP + message.NICKNAME + " is now " + message.TEXT_CONTENT + ".", "nickname_message");
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
