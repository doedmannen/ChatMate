package client.clientApp.controllers;

import client.clientApp.Client;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import models.Channel;
import models.Message;
import models.MessageType;
import models.User;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

public class MainGUIController {

   @FXML
   private TextField input_text;

   @FXML
   private TextField nickname_change;

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
      send_button.setOnAction(e -> sendMessage());
      add_channel_button.setOnAction(e -> addCannel());
      nickname_change.setOnAction(e -> changeNickName());
      scroll_pane.vvalueProperty().bind(chat_box.heightProperty());
      Createchanellist();
      createContextMenuForLeavingChannel();
      Client.getInstance().connect("localhost");
   }

   @FXML
   public void printUsers() {
      ConcurrentSkipListSet<User> users = Client.getInstance().channelList.get(Client.getInstance().getCurrentChannel());
      if (users != null) {
         users.stream()
                 .sorted(Comparator.comparing(User::getNickName))
                 .forEach(user -> {
                    Label label = new Label();
                    if (user.getID() == Client.getInstance().getThisUser().getID()) {
                       label.setFont(Font.font(null, FontWeight.BOLD, 12));
                       label.setText("(you) " + user.getNickName());
                    } else {
                       label.setText(user.getNickName());
                    }
                    label.setMinHeight(20);
                    online_list.getChildren().add(label);
                 });
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
   private void addCannel() {
      Message message = new Message();
      message.CHANNEL = channel_textField.getText();
      message.TYPE = MessageType.JOIN_CHANNEL;
      Client.getInstance().sender.sendToServer(message);
      channel_textField.clear();
   }

   @FXML
   private void Createchanellist() {
      channels = FXCollections.observableArrayList();
      SortedList<Channel> sortedList = new SortedList<>(channels, Comparator.comparing(Channel::getName));
      channel_list_view.setItems(sortedList);
      channel_list_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

      channel_list_view.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Channel>) (observable, oldValue, newValue) -> {
         chat_box.getChildren().clear();
         if (newValue != null) {
            Client.getInstance().setCurrentChannel(newValue.getName());
            Client.getInstance().getChannelMessages().get(newValue.getName()).forEach(l -> {
               chat_box.getChildren().add(l);
            });
         }
         Client.getInstance().changeTitle();
         refreshUserList();
      });
   }

   @FXML
   private void createContextMenuForLeavingChannel() {

      listContextMenu = new ContextMenu();
      MenuItem deleteMenuItem = new MenuItem("Leave Channel");
      deleteMenuItem.setOnAction((e) -> {
         if (channels.size() > 1) {
            Channel channel = (Channel) channel_list_view.getSelectionModel().getSelectedItem();
            Message message = new Message(MessageType.LEAVE_CHANNEL);
            Client.getInstance().sender.sendToServer(message);
            message.CHANNEL = ((Channel) channel_list_view.getSelectionModel().getSelectedItem()).getName();
            channels.remove(channel);
         }
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

   @FXML
   private void changeNickName() {
      String nickname = nickname_change.getText().trim();
      Message m = new Message(MessageType.NICKNAME_CHANGE);
      m.TEXT_CONTENT = nickname;
      Client.getInstance().sender.sendToServer(m);
      nickname_change.clear();
   }
}
