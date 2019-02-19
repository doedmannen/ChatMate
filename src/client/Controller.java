package client;

import client.clientApp.Client;
import client.clientApp.Sender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import models.Channel;
import models.Message;
import models.MessageType;
import models.User;

import java.awt.event.ActionEvent;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public class Controller {

    @FXML
    private TextField input_text;

    @FXML
    private Button send_button;

    @FXML
    private VBox online_list;

    @FXML
    private VBox chat_box;

    @FXML
    private ScrollPane scroll_pane;

    @FXML
    private Button add_channel_button;

    @FXML
    private TextField channel_textField;

    @FXML
    private ListView channel_list_view;
    @FXML
    private ContextMenu listContextMenu;

    public VBox getChatBox() {
        return chat_box;
    }

    public ObservableList<Channel> channels;


    public void initialize() {
        input_text.setOnAction(e -> sendMessage());
        channel_textField.setOnAction(event -> addCannel());
        send_button.setOnAction(e -> printUsers());
        add_channel_button.setOnAction(e -> addCannel());
        scroll_pane.vvalueProperty().bind(chat_box.heightProperty());
        Createchanellist();
        delitefromChanelList();
    }

    @FXML
    public void printUsers() {
        for (User user : Client.getInstance().channelList.get(Client.getInstance().getCurrentChannel())) {
            Label label = new Label();
            label.setText(user.getNickName());
            label.setMinHeight(20);
            online_list.getChildren().add(label);
        }
    }

    @FXML
    public void refreshUserList() {
        online_list.getChildren().clear();
        printUsers();
    }

    @FXML
    private void sendMessage() {
        final String status = input_text.getText();
        //// TODO: 2019-02-15 Create constructor for message
        Message message = new Message();
        message.CHANNEL = Client.getInstance().getCurrentChannel();
        message.TYPE = MessageType.CHANNEL_MESSAGE;
        message.TEXT_CONTENT = status;
        message.NICKNAME = Client.getInstance().getThisUser().getNickName();
        input_text.clear();
        Client.getInstance().sender.sendToServer(message);
    }

    @FXML
    private void addCannel(){
        //System.out.println("Knap tryckt");
        Message message = new Message();
        message.CHANNEL = channel_textField.getText();
        message.TYPE =  MessageType.JOIN_CHANNEL;
        Client.getInstance().sender.sendToServer(message);
        //channel_textField.clear();
    }
    @FXML
    private void Createchanellist(){
        channels = FXCollections.observableArrayList();
        SortedList<Channel> sortedList = new SortedList<>(channels, Comparator.comparing(Channel::getName));
        channel_list_view.setItems(sortedList);
        channel_list_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        channels.add(new Channel("test"));
        channels.add(new Channel("Java"));
        channels.add(new Channel("Te"));
        channels.add(new Channel("Colla"));

        channel_list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Channel>() {
            @Override
            public void changed(ObservableValue<? extends Channel> observable, Channel oldValue, Channel newValue) {
                Client.getInstance().setCurrentChannel(newValue.getName());
                scroll_pane.setContent(null);
                //System.out.println(newValue.getName());
            }
        });
    }

    @FXML
    private void delitefromChanelList(){
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delite");
        deleteMenuItem.setOnAction((e)->{
            Channel channel = (Channel) channel_list_view.getSelectionModel().getSelectedItem();
            channels.remove(channel);
            System.out.println("To delete: " + channel.getName());


        });
        listContextMenu.getItems().addAll(deleteMenuItem);
        channel_list_view.setCellFactory(new Callback<ListView<Channel>, ListCell<Channel>>() {
            @Override
            public ListCell<Channel> call(ListView<Channel> param) {
                ListCell<Channel> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Channel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        cell.setContextMenu(null);
                    } else {
                        cell.setContextMenu(listContextMenu);
                    }
                });

                return cell;
            }
        });

    }
}
