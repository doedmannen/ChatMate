package models;

import java.io.Serializable;
import java.util.UUID;


// TODO: 2019-02-13 Maybe implement comparable interface
// TODO: 2019-02-12 Undersök vad vi behöver för trädsortering.
public class User implements Serializable, Comparable<User> {

   private final UUID ID;
   private String nickName;

   public User(String nickName) {
      this.nickName = nickName;
      this.ID = UUID.randomUUID();
   }

   @Override
   public int hashCode() {
      return this.ID.hashCode() + 57;
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

      if (obj instanceof User) {
         User u = (User) obj;
         return this.ID.equals(u.ID);
      }

      return false;
   }

   @Override
   public int compareTo(User o) {
      return this.nickName.compareTo(o.getNickName());
   }

   public UUID getID() {
      return ID;
   }

   public String getNickName() {
      return nickName;
   }
}
