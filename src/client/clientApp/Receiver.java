package client.clientApp;

import client.Main;
import client.clientApp.Client;
import client.clientApp.MessageInboxHandler;
import client.Controller;
import models.Channel;
import models.Message;
import models.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Receiver extends Thread {
   private Socket socket;
   private ObjectInputStream objectInputStream;
   private Controller controller = (Controller) Main.primaryStage.getUserData();

   public Receiver(Socket socket) {
      System.out.println(controller);
      this.socket = socket;
      try {
         this.objectInputStream = new ObjectInputStream(socket.getInputStream());
      } catch (IOException e) {
         System.out.println("failed to create input stream");
         Client.getInstance().setIsRunning(false);
         // todo kolla om server är död, prova återanslutning
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public void run() {
      while (Client.getInstance().isRunning()) {
         try {
            Sendable inData = (Sendable) objectInputStream.readObject();
            if (inData instanceof Message) {
               Message message = (Message) inData;
               MessageInboxHandler.getInstance().messageSwitch(message);
            } else if (inData instanceof Channel) {
               Channel channel = (Channel) inData;
               Client.getInstance().channelList.put(channel.getName(), channel.getUsers());
               MessageInboxHandler.getInstance().process_CHANNELL(channel);
            }
         } catch (IOException e) {
            System.out.println("Read Error");
            Client.getInstance().setIsRunning(false);
            // todo kolla om server är död, prova återanslutning
         } catch (ClassNotFoundException e) {
            System.out.println("Message Error");
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
}
