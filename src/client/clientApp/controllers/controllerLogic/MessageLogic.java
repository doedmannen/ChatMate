package client.clientApp.controllers.controllerLogic;

import client.ClientMain;
import client.clientApp.Client;
import client.clientApp.controllers.ChatWindowController;
import models.Message;
import models.MessageType;
import models.User;

import java.util.Comparator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageLogic {
    ChatWindowController chatWindowController = (ChatWindowController) ClientMain.primaryStage.getUserData();

    public void sendMessage(String input) {
        boolean msgIsOk = false;
        String textToBeSent = input;
        Message message = new Message();
        message.CHANNEL = Client.getInstance().getCurrentChannel();
        // Check if message is a whisper or channel message
        if (textToBeSent.trim().toLowerCase().startsWith("/w")) {
            message.TYPE = MessageType.WHISPER_MESSAGE;
            String regexUsername;
            String newTextToBeSent;
            try {
                Pattern pattern = Pattern.compile("^\\/w (\\S{3,}) (\\D+)$");
                Matcher matcher = pattern.matcher(textToBeSent);
                matcher.matches(); // ????
                regexUsername = matcher.group(1);
                newTextToBeSent = matcher.group(2);

                UUID uuid = Client.getInstance().channelList.get(Client.getInstance().getCurrentChannel())
                        .stream().filter(u -> u.getNickName().equals(regexUsername)).toArray(User[]::new)[0]
                        .getID();
                message.RECEIVER = uuid;
                textToBeSent = newTextToBeSent;
                msgIsOk = true;
            } catch (Exception e) {
            }
        } else {
            message.TYPE = MessageType.CHANNEL_MESSAGE;
            msgIsOk = true;
        }
        message.TEXT_CONTENT = textToBeSent;
        if (msgIsOk) {
            Client.getInstance().sender.sendToServer(message);
        }
    }
}
