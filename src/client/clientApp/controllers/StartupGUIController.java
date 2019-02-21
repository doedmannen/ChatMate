package client.clientApp.controllers;

import client.clientApp.Client;
import client.clientApp.util.FileManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.User;
import models.UserData;
import org.w3c.dom.Text;

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
         }
      } catch (Exception e) {
         e.printStackTrace();
         this.data = new UserData();
      }
   }

   @FXML
   private void connectBtnPressed() {
      System.out.println("Btn pressed");
      if (validateInput()) {
         if (Client.getInstance().connect(serverAdressTextField.getText())) {
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
}
