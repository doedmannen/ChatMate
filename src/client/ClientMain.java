package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientMain extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientMain.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("clientApp/views/Startup_Window.fxml"));
        Parent root = loader.load();

        primaryStage.getIcons().add(new Image("/client/clientApp/images/logo.png"));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Chatter Matter");
        primaryStage.setScene(new Scene(root, 380, 150));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
