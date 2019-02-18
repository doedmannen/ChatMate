package server.serverApp;

import models.Message;
import models.MessageType;
import models.Sendable;
import models.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class ClientHandler implements Runnable {

   private ObjectOutputStream streamOut;
   private ObjectInputStream streamIn;
   private Socket socket;
   private ServerApp serverApp;
   private boolean isRunning = false;
   private final int TIMEOUT_MS = 10;
   private final User user;
   private final LinkedBlockingDeque<Sendable> userOutbox;
   private final LinkedBlockingQueue<Sendable> messageHandlerQueue;
   private final int RETRY_CONNECTION = 1000;

   public ClientHandler(Socket socket, ServerApp serverApp, LinkedBlockingQueue<Sendable> messageHandlerQueue) {
      this.socket = socket;
      this.serverApp = serverApp;
      this.user = new User("Unknown");
      this.userOutbox = new LinkedBlockingDeque<>();
      this.messageHandlerQueue = messageHandlerQueue;

      ActiveUserController.getInstance().addUser(this.user, this.userOutbox);
      System.out.println("All connected users: " + ActiveUserController.getInstance().getUsers().size());

      try {
         streamIn = new ObjectInputStream(socket.getInputStream());
         streamOut = new ObjectOutputStream(socket.getOutputStream());
         socket.setSoTimeout(TIMEOUT_MS);
         isRunning = true;
      } catch (IOException e) {
         System.out.println("Failed to create stream for client " + socket.getInetAddress());
      } catch (Exception e) {
         e.printStackTrace();
      }

      Message m = new Message.MessageBuilder(MessageType.CONNECT).build();
//      m.RECIVER = this.user.getID();

      this.userOutbox.add(m);

      System.out.println(socket.getInetAddress().toString() + " connected");
   }

   private void readMessage() {
      int stop = 1;
      for (int i = 0; i < stop; i++) {
         try {
            Message message = (Message) streamIn.readObject();
            message.setSender(this.user.getID());
            streamOut.writeObject(message);
            // System.out.println(message);//Debug
            messageHandlerQueue.add(message);
            i = 0;
            stop = 1;
         } catch (SocketTimeoutException e) {
         } catch (IOException e) {
            try{
               Thread.sleep(RETRY_CONNECTION);
            }catch (Exception ex){}
            stop = 10;

            if(i == stop - 1) {
               tryDisconnect();
            }
         } catch (ClassNotFoundException e) {
            System.out.println("Felaktig klass skickad"); // ENDAST DEBUG, tas bort sen eller ändras
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

   private void writeMessage() {
      int stop = 1;
      for (int i = 0; i < stop; i++) {
         while (clientHasMessages() && this.isRunning) {
            Sendable m = this.userOutbox.getFirst();
            try {
               streamOut.writeObject(m);
               this.userOutbox.removeFirst();
               i = 0;
               stop = 1;
            } catch (IOException e) {
               try {
                  Thread.sleep(RETRY_CONNECTION);
               } catch (Exception ex) {
               }
               stop = 10;
               if (i == stop - 1) {
                  tryDisconnect();
               }
               break;
            } catch (Exception e) {
               e.printStackTrace();
            }
         }

      }
   }

   private boolean clientHasMessages() {
      return userOutbox.size() > 0;
   }

   private void tryDisconnect() {
      cleanUpAfterUser();
      this.isRunning = false;
      System.out.println("Connection to " + socket.getInetAddress() + " lost");
      System.out.println("All connected users: " + ActiveUserController.getInstance().getUsers().size());
   }

   private void cleanUpAfterUser(){
      String[] userChannels = ActiveChannelController.getInstance().getChannelsForUser(this.user);
      Stream.of(userChannels).forEach(c -> {
         Message message = new Message.MessageBuilder(MessageType.DISCONNECT)
                 .toChannel(c)
                 .fromSender(this.user.getID())
                 .build();
         this.messageHandlerQueue.add(message);
      });
      ActiveUserController.getInstance().removeUser(this.user);
      try {
         this.socket.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   public void run() {
      while (serverApp.isRunning() && this.isRunning) {
         readMessage();
         writeMessage();
      }
   }
}
