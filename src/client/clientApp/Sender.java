package client.clientApp;

import models.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Sender extends Thread {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;

    public Sender(Socket socket) {
        this.socket = socket;
        try{
            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e){
            System.out.println("failed to create stream");
            Client.isRunning = false;
            // todo kolla om server är död, prova återanslutning
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (Client.isRunning) {
            try{
                objectOutputStream.writeObject(new Message());
                Thread.sleep(5000);
            }
            catch (Exception e){
                Client.isRunning = false;
                // todo kolla om server är död, prova återanslutning
            }
        }

    }

}
