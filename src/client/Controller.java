package client;

import client.clientApp.Client;
import client.clientApp.Sender;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import models.Message;
import models.MessageType;
import models.User;

import java.util.Iterator;

public class Controller {

    @FXML
    private TextField input_text;

    @FXML
    private TextArea output_text;

    @FXML
    private Button send_button;

    @FXML
    private VBox online_list;

    @FXML
    private VBox chat_box;

    @FXML
    private VBox online_Chanel;

    @FXML
    private Label curent_channel;

    @FXML
    private ScrollPane scroll_pane;

    public VBox getChatBox() {
        return chat_box;
    }

    public void initialize() {
        input_text.setOnAction(e -> sendMessage());
        send_button.setOnAction(e -> printUsers());
        scroll_pane.vvalueProperty().bind(chat_box.heightProperty());
        channelYouIn();
    }

    @FXML
    public void printUsers(){
        for (User user : Client.getInstance().channelList.get(Client.getInstance().getCurrentChannel())) {
            Label label = new Label();
            label.setText(user.getNickName());
            label.setMinHeight(20);
            online_list.getChildren().add(label);
        }
    }

    @FXML
    private void sendMessage(){
        final String status = input_text.getText();
        //// TODO: 2019-02-15 Create constructor for message
        Message message = new Message();
        message.CHANNEL = Client.getInstance().getCurrentChannel();
        message.TYPE = MessageType.CHANNEL_MESSAGE;
        message.TEXT_CONTENT = status;
        message.NICKNAME = Client.getInstance().getNickname();
        input_text.clear();
        Client.getInstance().sender.sendToServer(message);
    }

    @FXML
    //Hämtar vartman är
    private void printChannel(){
        String channelname = Client.getInstance().getCurrentChannel();
        //Vart som detta ska skrivas ut?
    }

    @FXML
    //Hämtar vart man tillhör
    private void channelYouIn(){
        Client.getInstance().channelList.keySet().stream().forEach(key -> {
            System.out.println(key);
            Label label = new Label();
            label.setText(key);
            online_Chanel.getChildren().add(label);
        });
    }

}
