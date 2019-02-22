package client;

import client.clientApp.Client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

        primaryStage.getIcons().add(new Image("/client/CSS/logo.png"));
        String css = this.getClass().getResource("/client/CSS/normal.css").toExternalForm();
        Platform.runLater(() ->primaryStage.getScene().getStylesheets().add(css));
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
