package client;

import client.clientApp.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
   public static Stage primaryStage;

   @Override
   public void start(Stage primaryStage) throws Exception {
      Main.primaryStage = primaryStage;

      Client.getInstance();

      FXMLLoader loader = new FXMLLoader(getClass().getResource("clientApp/views/Startup_GUI.fxml"));
      Parent root = loader.load();
      primaryStage.setResizable(false);
      primaryStage.setTitle("Chatter Matter");
      primaryStage.setScene(new Scene(root, 380, 150));
      primaryStage.show();
   }


   public static void main(String[] args) {
      launch(args);
   }
}
