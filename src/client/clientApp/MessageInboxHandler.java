package client.clientApp;

import client.Controller;
import client.Main;
import javafx.application.Platform;
import javafx.scene.control.Label;
import models.*;

import java.util.ArrayList;
import java.util.UUID;

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
        if(senderShouldBeIgnored(message.SENDER))
            return;
        Platform.runLater(() -> {
            switch (message.TYPE) {
                case WHISPER_MESSAGE:
                case CHANNEL_MESSAGE:
                case ERROR:
                case WARNING:
                    printLabelOnClient(message);
                    addMessageToList(message);
                    break;
                case JOIN_CHANNEL:
                    addMessageToList(message);
                    addUserToList(message);
                    printLabelOnClient(message);
                    controller.refreshUserList();
                    break;
                case LEAVE_CHANNEL:
                case DISCONNECT:
                    removeUserFromList(message);
                    addMessageToList(message);
                    printLabelOnClient(message);
                    controller.refreshUserList();
                    break;
                case NICKNAME_CHANGE:
                    changeNickname(message);
                    printLabelOnClient(message);
                    addMessageToList(message);
                    controller.refreshUserList();
                    break;
                case CONNECT:
                    connect(message);
                    break;
            }
        });
    }

    public void printLabelOnClient(Message message) {
        Label label = new MessageCreator().createLabel(message);
        if (message.CHANNEL.equals(Client.getInstance().getCurrentChannel())) {
            controller.getChatBox().getChildren().add(label);
        }
    }
    public void addMessageToList(Message message) {
        Label label = new MessageCreator().createLabel(message);
        Client.getInstance().getChannelMessages().get(message.CHANNEL).add(label);
    }

    public void addChannel(Channel channel) {
        ArrayList<Label> list = Client.getInstance().getChannelMessages().getOrDefault(channel.getName(), new ArrayList<>());
        Client.getInstance().getChannelMessages().put(channel.getName(), list);
        Platform.runLater(() -> controller.channels.add(channel));
    }

    public void changeNickname(Message message) {
        if (message.SENDER == Client.getInstance().getThisUser().getID()) {
            Client.getInstance().getThisUser().setNickName(message.TEXT_CONTENT);
            Client.getInstance().changeTitle();
        }
        Client.getInstance().channelList.get(message.CHANNEL).forEach(user -> {
            if (user.getID() == message.SENDER) {
                user.setNickName(message.TEXT_CONTENT);
            }
        });
    }

    public void connect(Message message){
        Client.getInstance().setThisUser(new User(message.NICKNAME, message.RECEIVER));
        Client.getInstance().changeTitle();
    }

    public void addUserToList(Message message) {
        Client.getInstance().channelList.get(message.CHANNEL).add(new User(message.NICKNAME, message.SENDER));
    }

    public void removeUserFromList(Message message) {
        User user = Client.getInstance().channelList.get(message.CHANNEL)
                .stream()
                .filter(u -> u.getID() == message.SENDER)
                .toArray(User[]::new)[0];
        Client.getInstance().channelList.get(message.CHANNEL).remove(user);
    }

    public void printUsers() {
        Platform.runLater(() -> controller.printUsers());
    }

    private boolean senderShouldBeIgnored(UUID uuid){
        return uuid != null && Client.getInstance().getUserIgnoreList().contains(uuid);
    }

}
