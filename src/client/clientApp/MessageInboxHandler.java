package client.clientApp;

import client.Controller;
import client.Main;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import models.Message;
import models.MessageType;
import models.Sendable;
import models.User;

public class MessageInboxHandler {
    private static MessageInboxHandler ourInstance = new MessageInboxHandler();

    public static MessageInboxHandler getInstance() {
        return ourInstance;
    }

    Controller controller;

    private MessageInboxHandler() {
        controller = (client.Controller) Main.primaryStage.getUserData();
    }

    public void messageSwitch(Message message) {
        MessageCreator messageCreator = new MessageCreator();
        Platform.runLater(() -> {
            switch (message.TYPE) {
                case CHANNEL_MESSAGE:
                    Client.getInstance().getChannelMessages().get(message.CHANNEL).add(message);
                    messageCreator.channelMessage(message);
                    break;
                case JOIN_CHANNEL:
                    Client.getInstance().channelList.get(message.CHANNEL).add(new User(message.NICKNAME, message.SENDER));
                    controller.refreshUserList();
                    messageCreator.joinChannelMessage(message);
                    break;
                case LEAVE_CHANNEL:
                    controller.refreshUserList();
                    messageCreator.leaveChannelMessage(message);
                    break;
                case DISCONNECT:
                    messageCreator.disconnectMessage(message);
                    break;
                case NICKNAME_CHANGE:
                    break;
                case WHISPER_MESSAGE:
                    messageCreator.whisperMessage(message);
                    break;
                case CONNECT:
                    break;
                case ERROR:
                    break;
                case WARNING:
                    messageCreator.warningMessage(message);
                    break;
            }
        });
    }

    public void printUsers() {
        Platform.runLater(() -> controller.printUsers());
    }

}
