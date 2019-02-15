package client.clientApp;

import models.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Reciever extends Thread{
    private Socket socket;
    private ObjectInputStream objectInputStream;
    public Reciever(Socket socket){
        this.socket=socket;
        try {
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e){
            System.out.println("failed to create input stream");
            TEMP_CLIENT.isRunning = false;
            // todo kolla om server är död, prova återanslutning
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (TEMP_CLIENT.isRunning){
            try {
                Message message = (Message) objectInputStream.readObject();
                System.out.println(message.TYPE.toString() + " : " + message.CHANNEL + " : " + message.TEXT_CONTENT);
            }catch (IOException e){
                System.out.println("Read Error");
                TEMP_CLIENT.isRunning = false;
                // todo kolla om server är död, prova återanslutning
            }catch (ClassNotFoundException e){
                System.out.println("Message Error");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
