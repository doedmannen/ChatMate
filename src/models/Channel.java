package models;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class Channel implements Comparable<Channel> {
   private String name;
   private ConcurrentSkipListSet<User> users;
   private UUID ID;

   public Channel(String name) {
      this.name = name;
      this.users = new ConcurrentSkipListSet<>();
      this.ID = UUID.randomUUID();
   }

   public String getName() {
      return name;
   }

   public SortedSet<User> getUsers() {
      return Collections.unmodifiableSortedSet(this.users);
   }

   public UUID getID() {
      return ID;

   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean addUser(User user) {
      return this.users.add(user);
   }

   public boolean removeUser(User user) {
      return this.users.remove(user);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      }

      if (obj instanceof UUID) {
         UUID ID = (UUID) obj;
         return this.ID.equals(ID);
      }

      if (obj instanceof Channel) {
         Channel c = (Channel) obj;
         return this.ID.equals(c.ID);
      }

      return false;
   }

   @Override
   public int compareTo(Channel o) {
      return this.name.compareTo(o.name);
   }
}
















