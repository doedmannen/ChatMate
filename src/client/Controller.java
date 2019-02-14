package client;

import client.clientApp.Client;
import client.clientApp.Sender;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Message;

public class Controller {

    @FXML
    private TextField input_text;

    public TextArea getOutput_text() {
        return output_text;
    }

    @FXML
    private TextArea output_text;

    @FXML
    private void sendMessage(){
        final String status = input_text.getText();
        Message message = new Message();
        message.TEXT_CONTENT = status;
        input_text.clear();
//        output_text.appendText(message.TEXT_CONTENT);
//        System.out.println(message.TEXT_CONTENT);
        Client.sender.sendToServer(message);
    }

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
//
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
