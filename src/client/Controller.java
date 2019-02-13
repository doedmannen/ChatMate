package client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private TextField input_text;

    public TextArea getOutput_text() {
        return output_text;
    }

    @FXML
    private TextArea output_text;

//    @FXML
//    public void startRead() {
//        // Create a Runnable
//        Runnable task = () -> runRead();
//
//        // Run the task in a background thread
//        Thread backgroundThread = new Thread(task);
//        // Terminate the running thread if the application exits
//        backgroundThread.setDaemon(true);
//        // Start the thread
//        backgroundThread.start();
//    }
//
//    public void runRead() {
//        try {
//            // Get the Status
//            final String status = input_text.getCharacters().toString();
//            input_text.clear();
//
//            // Update the Label on the JavaFx Application Thread
////                Platform.runLater(() -> statusLabel.setText(status));
//            output_text.appendText(status + "\n");
//
//
//
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}
