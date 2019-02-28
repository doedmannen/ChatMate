package client.clientApp.controllers.controllerLogic;

import client.clientApp.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import models.User;

import java.util.Comparator;

public class UserLogic {

    public SortedList<User> printUsers(){
        String channel = Client.getInstance().getCurrentChannel();
        ObservableList<User> users = FXCollections.observableArrayList();
        if (channel != null) {
            users.addAll(Client.getInstance().channelList.get(channel));
        }
        return new SortedList<>(users, Comparator.comparing(User::getNickName));
    }
}
