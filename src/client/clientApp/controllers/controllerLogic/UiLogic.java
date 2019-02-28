package client.clientApp.controllers.controllerLogic;

import client.ClientMain;
import javafx.application.Platform;

public class UiLogic {
    public void loadCss() {
        String css = this.getClass().getResource("/client/clientApp/css/normal.css").toExternalForm();
        Platform.runLater(() -> ClientMain.primaryStage.getScene().getStylesheets().add(css));
    }

    public void toggleDarkMode(Boolean checkbox) {
        String darkmodeCss = this.getClass().getResource("/client/clientApp/css/darkmode.css").toExternalForm();
        String normalCss = this.getClass().getResource("/client/clientApp/css/normal.css").toExternalForm();
        Platform.runLater(() ->
        {
            if (checkbox) {
                ClientMain.primaryStage.getScene().getStylesheets().remove(normalCss);
                ClientMain.primaryStage.getScene().getStylesheets().add(darkmodeCss);
            } else {
                ClientMain.primaryStage.getScene().getStylesheets().remove(darkmodeCss);
                ClientMain.primaryStage.getScene().getStylesheets().add(normalCss);
            }
        });
    }
}
