package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {

    @FXML
    private TextField input_text;
    @FXML
    private TextArea output_text;

    public void inputText() {
        input_text.setOnAction(event -> startTask());
    }

    @FXML
    public void startTask()
    {
        // Create a Runnable
        Runnable task = () -> runTask();

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }
    public void runTask()
    {
        for(int i = 1; i <= 10; i++)
        {
            try
            {
                // Get the Status
                final String status = "Processing " + i + " of " + 10;

                // Update the Label on the JavaFx Application Thread
//                Platform.runLater(() -> statusLabel.setText(status));

                output_text.appendText(status+"\n");

                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
