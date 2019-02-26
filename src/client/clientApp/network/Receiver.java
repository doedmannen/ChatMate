package client.clientApp.network;

import client.clientApp.Client;
import client.clientApp.MessageInboxHandler;
import models.Channel;
import models.Message;
import models.Sendable;
import models.Encryption;

import javax.crypto.SealedObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class Receiver extends Thread {
   private Socket socket;
   private ObjectInputStream objectInputStream;
   private Encryption decrypt;

   public Receiver(Socket socket) {
      this.socket = socket;
      this.decrypt = new Encryption();
      try {
         this.objectInputStream = new ObjectInputStream(socket.getInputStream());
      } catch (IOException e) {
         System.out.println("Failed to create input stream");
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
            SealedObject encryptedObject = (SealedObject) objectInputStream.readObject();
            Sendable inData = (Sendable) decrypt.decryptObject(encryptedObject);
            if (inData instanceof Message) {
               Message message = (Message) inData;
               MessageInboxHandler.getInstance().messageSwitch(message);
            } else if (inData instanceof Channel) {
               Channel channel = (Channel) inData;
               Client.getInstance().channelList.put(channel.getName(), channel.getUsers());
               MessageInboxHandler.getInstance().addChannel(channel);
            }
         } catch (IOException e) {
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
