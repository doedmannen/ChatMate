package client.clientApp;

import client.Controller;
import client.Main;
import models.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import static client.Main.primaryStage;

public class Reciever extends Thread{
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private Controller controller = (client.Controller) primaryStage.getUserData();
    public Reciever(Socket socket){
        this.socket=socket;
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            System.out.println("failed to create input stream");
            Client.isRunning = false;
            // todo kolla om server är död, prova återanslutning
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (Client.isRunning){
            try {
                Message message = (Message) objectInputStream.readObject();
                controller.getOutput_text().appendText(message.TEXT_CONTENT + "\n"); //For debugging javaFX print
//                System.out.println(message);
            }catch (IOException e){
                System.out.println("Read Error");
                Client.isRunning = false;
                // todo kolla om server är död, prova återanslutning
            }catch (ClassNotFoundException e){
                System.out.println("Message Error");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
