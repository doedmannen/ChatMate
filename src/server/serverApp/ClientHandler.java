package server.serverApp;

import models.Message;
import models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientHandler implements Runnable {

   private ObjectOutputStream streamOut;
   private ObjectInputStream streamIn;
   private Socket socket;
   private ServerApp serverApp;
   private boolean isRunning = false;
   private final int TIMEOUT_MS = 500;
   private final User user;
   private final LinkedBlockingDeque<Message> userOutbox;

   public ClientHandler(Socket socket, ServerApp serverApp) {
      this.socket = socket;
      this.serverApp = serverApp;
      this.user = new User("Unknown");
      this.userOutbox = new LinkedBlockingDeque<>();

      ActiveUserController.getInstance().addUser(this.user, this.userOutbox);

      try {
         streamIn = new ObjectInputStream(socket.getInputStream());
         streamOut = new ObjectOutputStream(socket.getOutputStream());
         //Flush?
         socket.setSoTimeout(TIMEOUT_MS);
         isRunning = true;
      } catch (IOException e) {
         System.out.println("failed to create streams ");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void readMessage() {
      try {
         Message message = (Message) streamIn.readObject();
         System.out.println(message);//Debug
      } catch (SocketTimeoutException e) {
      } catch (IOException e) {
         // Kolla om möjlig återanslutning till server
         System.out.println("Error in Clientreader");
         tryReconnect();
         // todo kolla om klienten är död, prova återanslutning
      } catch (ClassNotFoundException e) {
         System.out.println("Felaktig klass skickad");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void writeMessage() {
      /// TODO: 2019-02-12 Hämta lista med clients meddelande och skicka dem.
      try {
         Message message = new Message();
         streamOut.writeObject(message);
      } catch (IOException e) {
         System.out.println("Error in clientWriter");
         tryReconnect();
         // todo kolla om klienten är död, prova återanslutning
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private boolean clientHasMessages() {
      return userOutbox.size() > 0;
   }

   private void tryReconnect() {
      ActiveUserController.getInstance().removeUser(this.user);
      this.isRunning = false;
      System.out.println("Connection lost");
   }

   @Override
   public void run() {
      while (serverApp.isRunning() && this.isRunning) {
         readMessage();
         //writeMessage();
      }
   }
}
