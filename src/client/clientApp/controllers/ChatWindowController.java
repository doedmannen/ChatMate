package client.clientApp.controllers;

import client.ClientMain;
import client.clientApp.Client;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import models.Channel;
import models.Message;
import models.MessageType;
import models.User;

import java.util.Comparator;

public class ChatWindowController {

   private Image notification = new Image("file:src/client/clientApp/images/notification.png");

   @FXML
   private TextField input_text;

   @FXML
   private TextField nickname_change;

   @FXML
   private Button send_button;

   @FXML
   private VBox chat_box;

   @FXML
   private ScrollPane scroll_pane;

   @FXML
   private Button add_channel_button;

   @FXML
   private TextField channel_textField;

   @FXML
   public ListView channel_list_view;

   @FXML
   private ContextMenu listContextMenu;

   @FXML
   public CheckBox darkmode_checkbox;

   private ContextMenu igonorelistContextMenu;

   @FXML
   private ListView now_online_list;

   public ObservableList<User> users;


   public VBox getChatBox() {
      return chat_box;
   }

   public ObservableList<Channel> channels;

   public void initialize() {
      loadCss();
      addTextLimiter(input_text, 1000);
      input_text.setOnAction(e -> sendMessage());
      channel_textField.setOnAction(event -> addChannel());
      send_button.setOnAction(e -> sendMessage());
      add_channel_button.setOnAction(e -> addChannel());
      nickname_change.setOnAction(e -> changeNickName());
      scroll_pane.vvalueProperty().bind(chat_box.heightProperty());
      createChannelList();
      createContextMenuForLeavingChannel();
      toggleDarkMode();
      createChannelList();
      createContextMenuForLeavingChannel();
      createContextMenuForUser();

      // needs to happen here otherwise it wont work
      // why? dont know
      Client.getInstance().startSenderAndReceiver();
      recreateOldSession();
   }

   private void addTextLimiter(final TextField tf, final int maxLength) {
      tf.textProperty().addListener((ov, oldValue, newValue) -> {
         if (tf.getText().length() > maxLength) {
            String s = tf.getText().substring(0, maxLength);
            tf.setText(s);
         }
      });
   }

   private void loadCss() {
      String css = this.getClass().getResource("/client/clientApp/css/normal.css").toExternalForm();
      Platform.runLater(() -> ClientMain.primaryStage.getScene().getStylesheets().add(css));
   }

   private void toggleDarkMode() {
      darkmode_checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
         darkmode_checkbox.setSelected(newValue);
         String darkmodeCss = this.getClass().getResource("/client/clientApp/css/darkmode.css").toExternalForm();
         String normalCss = this.getClass().getResource("/client/clientApp/css/normal.css").toExternalForm();
         Platform.runLater(() ->
         {
            if (darkmode_checkbox.isSelected()) {
               ClientMain.primaryStage.getScene().getStylesheets().remove(normalCss);
               ClientMain.primaryStage.getScene().getStylesheets().add(darkmodeCss);
            } else {
               ClientMain.primaryStage.getScene().getStylesheets().remove(darkmodeCss);
               ClientMain.primaryStage.getScene().getStylesheets().add(normalCss);
            }
         });
      });
   }


   @FXML
   public void printUsers() {
      String channel = Client.getInstance().getCurrentChannel();
      users = FXCollections.observableArrayList();
      if (channel != null) {
         users.addAll(Client.getInstance().channelList.get(channel));
      }
      SortedList<User> sortedList = new SortedList<>(users, Comparator.comparing(User::getNickName));
      now_online_list.setItems(sortedList);
      now_online_list.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
   }

   @FXML
   public void createContextMenuForUser() {
      igonorelistContextMenu = new ContextMenu();
      MenuItem ignoreMenuItem = new MenuItem("Toggle ignore");
      ignoreMenuItem.setOnAction((e) -> {
         User user = (User) now_online_list.getSelectionModel().getSelectedItem();
         Client.getInstance().toggleIgnoreOnUser(user.getID());
      });
      MenuItem wisperMenuItem = new MenuItem("Whisper");
      wisperMenuItem.setOnAction((e) -> {
         System.out.println("Inte så högt!!!");
      });
      igonorelistContextMenu.getItems().addAll(ignoreMenuItem, wisperMenuItem);
      now_online_list.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
         @Override
         public ListCell<User> call(ListView<User> param) {
            ListCell<User> cell = new ListCell<>() {
               @Override
               protected void updateItem(User item, boolean empty) {
                  super.updateItem(item, empty);
                  if (empty) {
                     setText(null);
                  } else {
                     setText(item.getNickName());
                  }
               }
            };
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
               if (isNowEmpty) {
                  cell.setContextMenu(null);
               } else {
                  cell.setContextMenu(igonorelistContextMenu);
               }
            });
            return cell;
         }
      });
   }


   @FXML
   public void refreshUserList() {
      //online_list.getChildren().clear();
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
   private void addChannel() {
      Message message = new Message();
      message.CHANNEL = channel_textField.getText();
      message.TYPE = MessageType.JOIN_CHANNEL;
      Client.getInstance().sender.sendToServer(message);
      channel_textField.clear();
   }

   @FXML
   private void createChannelList() {
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
               Client.getInstance().getUncheckedChannels().remove(newValue.getName());
               channel_list_view.refresh();
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
         Channel channel = (Channel) channel_list_view.getSelectionModel().getSelectedItem();
         Message message = new Message(MessageType.LEAVE_CHANNEL);
         Client.getInstance().sender.sendToServer(message);
         message.CHANNEL = ((Channel) channel_list_view.getSelectionModel().getSelectedItem()).getName();
         channels.remove(channel);
         if (channels.size() == 0) {
            chat_box.getChildren().clear();
            Client.getInstance().setCurrentChannel(null);
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
                     setGraphic(null);
                  } else {
                     if (Client.getInstance().getUncheckedChannels().contains(item.getName())) {
                        ImageView imageView = new ImageView();
                        imageView.setImage(notification);
                        setText(item.getName());
                        setGraphic(null);
                        setGraphic(imageView);
                     } else {
                        setText(item.getName());
                        setGraphic(null);

                     }
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


   private void recreateOldSession() {
      this.darkmode_checkbox.setSelected(Client.getInstance().getUserData().isDarkMode());

      Client.getInstance().setChannelMessages(Client.getInstance().getUserData().getChannelMessages());

      Message nickChangeMessage = new Message(MessageType.NICKNAME_CHANGE);
      nickChangeMessage.TEXT_CONTENT = Client.getInstance().getUserData().getUsername();
      Client.getInstance().sender.sendToServer(nickChangeMessage);

      Client.getInstance().getUserData().getJoinedChannels().forEach(c -> {
         Message channelJoinMessage = new Message(MessageType.JOIN_CHANNEL);
         channelJoinMessage.CHANNEL = c;
         Client.getInstance().sender.sendToServer(channelJoinMessage);
      });
   }

   @FXML
   private void changeNickName() {
      String nickname = nickname_change.getText();
      Message m = new Message(MessageType.NICKNAME_CHANGE);
      m.TEXT_CONTENT = nickname;
      m.CHANNEL = Client.getInstance().getCurrentChannel();

      Client.getInstance().sender.sendToServer(m);
      nickname_change.clear();
   }

   public ListView getChannel_list_view() {
      return channel_list_view;
   }
}
