package client.clientApp.controllers;

import client.ClientMain;
import client.clientApp.Client;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import models.Channel;
import models.Message;
import models.MessageType;
import models.User;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class ChatWindowController {

   private Image notification = new Image("file:src" + File.separator + "client" + File.separator + "clientApp" + File.separator + "images" + File.separator + "notification.png");

   @FXML
   private TextField input_text;

   @FXML
   public CheckBox mute_checkbox;

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
   private VBox image_icon;

   @FXML
   private ListView now_online_list;

   private ObservableList<User> users;


   public VBox getChatBox() {
      return chat_box;
   }

   public ObservableList<Channel> channels;

   public void initialize() {
      loadCss();
      addTextLimiter(input_text, 200);
      input_text.setOnAction(e -> sendMessage());
      channel_textField.setOnAction(event -> addChannel());
      send_button.setOnAction(e -> sendMessage());
      add_channel_button.setOnAction(e -> addChannel());
      nickname_change.setOnAction(e -> changeNickName());
      scroll_pane.vvalueProperty().bind(chat_box.heightProperty());
      image_icon.setOnMouseClicked(e -> lurig());
      createChannelList();
      createContextMenuForLeavingChannel();
      toggleDarkMode();
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


   @FXML
   public void createContextMenuForUser() {
      igonorelistContextMenu = new ContextMenu();
      MenuItem ignoreMenuItem = new MenuItem("Toggle ignore");
      ignoreMenuItem.setOnAction((e) -> {
         User user = (User) now_online_list.getSelectionModel().getSelectedItem();
         Client.getInstance().toggleIgnoreOnUser(user.getID());
         refreshUserList();
      });
      MenuItem wisperMenuItem = new MenuItem("Whisper");
      wisperMenuItem.setOnAction((e) -> {
         User user = (User) now_online_list.getSelectionModel().getSelectedItem();
         input_text.clear();
         input_text.setText("/w " + user.getID() + " ");
         input_text.requestFocus();
         input_text.forward();
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
                     setOpacity(1);
                     if (Client.getInstance().getThisUser().getID().equals(item.getID())) {
                        setText("[me] " + item.getNickName());
                     } else if (Client.getInstance().userIsIgnored(item.getID())) {
                        setText("[i] " + item.getNickName());
                        setOpacity(0.3);
                     } else {
                        setText(item.getNickName());
                     }
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
   private void sendMessage() {
      boolean msgIsOk = false;
      String textToBeSent = input_text.getText();
      Message message = new Message();
      message.CHANNEL = Client.getInstance().getCurrentChannel();
      input_text.clear();
      // Check if message is a whisper or channel message
      if (textToBeSent.trim().toLowerCase().startsWith("/w")) {
         message.TYPE = MessageType.WHISPER_MESSAGE;
         try {
            message.RECEIVER = UUID.fromString(textToBeSent.substring(3, 39));
            input_text.appendText(textToBeSent.substring(0, 39).concat(" "));
            textToBeSent = textToBeSent.substring(39);
            msgIsOk = true;
         } catch (Exception e) {
         }
      } else {
         message.TYPE = MessageType.CHANNEL_MESSAGE;
         msgIsOk = true;
      }
      message.TEXT_CONTENT = textToBeSent;
      input_text.requestFocus();
      input_text.forward();
      if (msgIsOk) {
         Client.getInstance().sender.sendToServer(message);
      }
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
   public void refreshUserList() {
      printUsers();
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
      if (Client.getInstance().getUserData().getJoinedChannels().size() > 0) {
         Client.getInstance().getUserData().getJoinedChannels().forEach(c -> {
            Message channelJoinMessage = new Message(MessageType.JOIN_CHANNEL);
            channelJoinMessage.CHANNEL = c;
            Client.getInstance().sender.sendToServer(channelJoinMessage);
         });
      } else {
         Message joinGeneral = new Message(MessageType.JOIN_CHANNEL);
         joinGeneral.CHANNEL = "General";
         Client.getInstance().sender.sendToServer(joinGeneral);
      }
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

   public TextField getInput_text() {
      return input_text;
   }

   @FXML
   private void deleteHistory() {
      Client.getInstance().getChannelMessages().clear();
      channels.forEach(c -> {
         Client.getInstance().getChannelMessages().put(c.getName(), new ArrayList<>());
      });
      chat_box.getChildren().clear();
      Client.getInstance().getUncheckedChannels().clear();
      channel_list_view.refresh();
   }

   @FXML
   private void logOut() throws Exception {
//      Client.getInstance().kill();
      Client.getInstance().saveData();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/clientApp/views/Startup_Window.fxml"));
      Parent startup = loader.load();

      ClientMain.primaryStage.getIcons().add(new Image("/client/clientApp/images/logo.png"));
      ClientMain.primaryStage.setResizable(false);
      ClientMain.primaryStage.setTitle("Chatter Matter");
      ClientMain.primaryStage.setScene(new Scene(startup, 380, 270));


      if (Client.getInstance().getUserData().isDarkMode()) {
         String darkmodeCss = this.getClass().getResource("/client/clientApp/css/darkmode.css").toExternalForm();
         ClientMain.primaryStage.getScene().getStylesheets().add(darkmodeCss);
      } else {
         String normalCss = this.getClass().getResource("/client/clientApp/css/normal.css").toExternalForm();
         ClientMain.primaryStage.getScene().getStylesheets().add(normalCss);
      }

      ClientMain.primaryStage.show();
   }

   @FXML
   private void tryReconnect(){
      System.out.println("Try Reconnecting");
   }

   public void lurig() {
      if (input_text.getText().equals("man") && Client.getInstance().getThisUser().getNickName().equals("spider")) {
         Platform.runLater(() -> {
            WebView webview = new WebView();
            webview.getEngine().load(
                    "https://www.youtube.com/embed/PaFnO5LKTSs?autoplay=1"
//                    PaFnO5LKTSs
//                    hj1Uc-vCoJM
            );
            webview.setPrefSize(640, 390);
            ClientMain.primaryStage.setScene(new Scene(webview));
            ClientMain.primaryStage.show();
         });
      }
   }
}

