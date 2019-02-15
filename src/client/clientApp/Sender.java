package client.clientApp;

import models.Message;
import models.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

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
            TEMP_CLIENT.isRunning = false;
            // todo kolla om server är död, prova återanslutning
        }
        catch (Exception e){
            e.printStackTrace();
        }

       Message join = new Message(MessageType.JOIN_CHANNEL);
       join.CHANNEL = "General";
       Message msg = new Message(MessageType.CHANNEL_MESSAGE);
       msg.CHANNEL = "General";
       msg.TEXT_CONTENT = "Hej hej på dig";
       messages.add(join);
       messages.add(msg);
    }

   private LinkedList<Message> messages = new LinkedList<>();
   @Override
   public void run() {
      while (TEMP_CLIENT.isRunning) {
         if(messages.size() > 0){
            try{
               objectOutputStream.writeObject(messages.removeFirst());
            }
            catch (Exception e){
               TEMP_CLIENT.isRunning = false;
               // todo kolla om server är död, prova återanslutning
            }
         }
         try{
            Thread.sleep(3000);
         }catch (Exception e){}
      }
   }

}
