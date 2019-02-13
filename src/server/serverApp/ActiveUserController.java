package server.serverApp;

import models.Message;
import models.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public class ActiveUserController {
   private static ActiveUserController ourInstance = new ActiveUserController();

   public static ActiveUserController getInstance() {
      return ourInstance;
   }

   private ConcurrentHashMap<User, LinkedBlockingDeque<Message>> connectedUsers = new ConcurrentHashMap<>();

   private ActiveUserController() {
   }

   public boolean addUser(User user, LinkedBlockingDeque<Message> outbox) {
      if (this.connectedUsers.containsKey(user)) {
         return false;
      }
      this.connectedUsers.put(user, outbox);
      return true;
   }

   public boolean removeUser(User user) {
      // TODO: 2019-02-13 Remove user from all channels

      return this.connectedUsers.remove(user) != null;
   }

   public LinkedBlockingDeque<Message> getUserOutbox(User user) {
      return connectedUsers.get(user);
   }

   public LinkedBlockingDeque getUserOutbox(UUID ID) {
      for (Map.Entry e : this.connectedUsers.entrySet()) {
         UUID entryID = ((User) e.getValue()).getID();
         if (entryID.equals(ID)) {
            return (LinkedBlockingDeque) e.getValue();
         }
      }
      return null;
   }

   public Map getUsers() {
      return Collections.unmodifiableMap(this.connectedUsers);
   }

}
