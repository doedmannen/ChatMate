package client.clientApp.controllers;

import client.Main;
import client.clientApp.Client;
import client.Controller;
import client.clientApp.util.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.UserData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartupGUIController {

   @FXML
   TextField nicknameTextField;
   @FXML
   TextField serverAdressTextField;
   @FXML
   Label errorLabel;

   private UserData data;

   public void initialize() {
      loadUserData();
      nicknameTextField.setText(data.getUsername());
   }

   private void loadUserData() {
      try {
         UserData data = (UserData) FileManager.loadFile("user-data.ser");
         if (data == null) {
            this.data = new UserData();
         } else {
            this.data = data;
            data.initialize();
         }
      } catch (Exception e) {
         e.printStackTrace();
         this.data = new UserData();
      }
   }

   @FXML
   private void connectBtnPressed() throws Exception {
      System.out.println("Btn pressed");
      if (validateInput()) {
         if (Client.getInstance().connect(serverAdressTextField.getText())) {
            Client.getInstance().setUserData(data);
            // TODO: 2019-02-21 Add set ignorelist here
            swtichWindow();
         } else {
            errorLabel.setText("Could not connect");
         }
      } else {
         errorLabel.setText("Wrong data");
      }
   }

   private boolean validateInput() {
      String nickname = nicknameTextField.getText().trim();
      nicknameTextField.setText(nickname);

      String serverAddress = serverAdressTextField.getText().trim();
      serverAdressTextField.setText(serverAddress);

      String nicknameRegex = "^[a-zA-z0-9-_]{3,15}$";
      Pattern pattern = Pattern.compile(nicknameRegex);
      Matcher nicknameMatcher = pattern.matcher(nickname);

      if (nicknameMatcher.matches() && !serverAddress.equals("")) {
         return true;
      }
      return false;
   }

   private void swtichWindow() throws Exception {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../../GUI.fxml"));
      Parent root = loader.load();
      Controller controller = loader.getController();
      Main.primaryStage.setUserData(controller);
      Client.getInstance();

      Main.primaryStage.setResizable(false);
      Main.primaryStage.setOnCloseRequest(e -> Client.getInstance().saveData());
      Main.primaryStage.setTitle("Chatter Matter");
      Main.primaryStage.setScene(new Scene(root, 900, 600));
   }
}
