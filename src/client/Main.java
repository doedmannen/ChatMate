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
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setUserData(controller);
        Client.getInstance();

        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> Client.getInstance().kill());
        primaryStage.setTitle("Chatter Matter");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
