package models;

import java.io.Serializable;
import java.util.*;

public class UserData implements Serializable {
   private HashSet<String> channels;
   private String username;
   private HashSet<UUID> ignoreList;

   public UserData() {
      this.channels = new HashSet<>();
      this.username = "Unknown";
      this.ignoreList = new HashSet<>();
   }

   public UserData(HashSet<String> channels, String username, HashSet<UUID> ignoreList) {
      this.channels = channels;
      this.username = username;
      this.ignoreList = ignoreList;
   }

   public UserData setUsername(String username) {
      this.username = username;
      return this;
   }

   public UserData addChannel(String channel) {
      this.channels.add(channel);
      return this;
   }

   public UserData addChannels(Collection<String> channels) {
      this.channels.addAll(channels);
      return this;
   }

   public UserData addIgnore(UUID ignoreID) {
      this.ignoreList.add(ignoreID);
      return this;
   }

   public UserData addIgnores(Collection<UUID> ignoreIDs) {
      this.ignoreList.addAll(ignoreIDs);
      return this;
   }

   public HashSet<String> getChannels() {
      return this.channels;
   }

   public String getUsername() {
      return username;
   }

   public HashSet<UUID> getIgnoreList() {
      return ignoreList;
   }
}
