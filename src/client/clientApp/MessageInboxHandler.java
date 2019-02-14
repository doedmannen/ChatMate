package client.clientApp;

import client.Controller;
import client.Main;
import models.Message;
import models.MessageType;
import models.Sendable;

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
        switch (message.TYPE){
            case CHANNEL_MESSAGE:
                controller.getOutput_text().appendText(message.TEXT_CONTENT + "\n");
                break;
            case JOIN_CHANNEL:
                break;
            case LEAVE_CHANNEL:
                break;
            case DISCONNECT:
                break;
            case NICKNAME_CHANGE:
                break;
            case WHISPER_MESSAGE:
                break;
        }
    }

}
