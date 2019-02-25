package client.clientApp.controllers;

import client.ClientMain;
import client.clientApp.Client;
import client.clientApp.util.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import models.UserData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartupWindowController {

   @FXML
   TextField nicknameTextField;
   @FXML
   TextField serverAdressTextField;
   @FXML
   Label errorLabel;

   private UserData data;

   public void initialize() {
      data = Client.getInstance().getUserData();;
      data.initialize();
      nicknameTextField.setText(data.getUsername());
      serverAdressTextField.setText(data.getIP());
   }

   @FXML
   private void connectBtnPressed() throws Exception {
      if (validateInput()) {
         if (Client.getInstance().connect(serverAdressTextField.getText())) {
            data.setUsername(nicknameTextField.getText());
            Client.getInstance().setUserData(data);
            // TODO: 2019-02-21 Add set ignorelist here
            swtichWindow();
         } else {
            errorLabel.setText("Could not connect");
         }
      }
   }

   private boolean validateInput() {
      String nickname = nicknameTextField.getText().trim();
      nicknameTextField.setText(nickname);

      String serverAddress = serverAdressTextField.getText().trim();
      serverAdressTextField.setText(serverAddress);

      String nicknameRegex = "^[a-zA-z0-9-_]{3,10}$";
      Pattern pattern = Pattern.compile(nicknameRegex);
      Matcher nicknameMatcher = pattern.matcher(nickname);

      if (!nicknameMatcher.matches()) {
         errorLabel.setText("Name should be 3-10 characters long containing only letter and/or numbers");
         nicknameTextField.selectAll();
         return false;
      }

      if (serverAddress.equals("")) {
         serverAdressTextField.selectAll();
         return false;
      } else {
         return true;
      }
   }

   private void swtichWindow() throws Exception {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Chat_Window.fxml"));
      Parent root = loader.load();
      ChatWindowController chatWindowController = loader.getController();
      ClientMain.primaryStage.setUserData(chatWindowController);
      Client.getInstance();

      ClientMain.primaryStage.setResizable(false);
      ClientMain.primaryStage.setOnCloseRequest(e -> Client.getInstance().saveData());
      ClientMain.primaryStage.setTitle("Chatter Matter");
      ClientMain.primaryStage.setScene(new Scene(root, 900, 600));
   }

   @FXML
   private void KeyReleasedOnNameField(KeyEvent e) throws Exception {
      if (e.getCode().toString().equals("ENTER")) {
         connectBtnPressed();
      }
   }

   @FXML
   private void KeyReleasedOnIPField(KeyEvent e) throws Exception {
      if (e.getCode().toString().equals("ENTER")) {
         connectBtnPressed();
      }
   }
}
