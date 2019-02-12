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
    }catch (Exception e){
        e.printStackTrace();
    }
    }

    @Override
    public void run() {
        while (TEMP_CLIENT.isRunning){
            try {
                Message message = (Message) objectInputStream.readObject();
                System.out.println(message);
            }catch (IOException e){
                System.out.println("Read Error");
                return; //todo retry connection
            }catch (ClassNotFoundException e){
                System.out.println("Message Error");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
