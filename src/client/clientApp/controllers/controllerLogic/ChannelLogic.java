package client.clientApp.controllers.controllerLogic;

import client.clientApp.Client;
import models.Message;
import models.MessageType;

public class ChannelLogic {
    public void addChannel(String channel) {
        Message message = new Message();
        message.CHANNEL = channel;
        message.TYPE = MessageType.JOIN_CHANNEL;
        Client.getInstance().sender.sendToServer(message);
    }
}
