package models;

import javafx.scene.control.Label;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserData implements Serializable {
   private ConcurrentHashMap<String, ArrayList<ChatLabel>> channelMessages;

   private String username;
   private HashSet<UUID> ignoreList;

   public UserData() {
      this.channelMessages = new ConcurrentHashMap<>();
      this.username = "Unknown";
      this.ignoreList = new HashSet<>();
   }

   public UserData(ConcurrentHashMap<String, ArrayList<ChatLabel>> channelMessages, String username, HashSet<UUID> ignoreList) {
      this.channelMessages = channelMessages;
      this.username = username;
      this.ignoreList = ignoreList;
   }

   public UserData setUsername(String username) {
      this.username = username;
      return this;
   }

   public UserData addChannel(String channel, ArrayList<ChatLabel> messages) {
      this.channelMessages.put(channel, messages);
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

   public ConcurrentHashMap<String, ArrayList<ChatLabel>> getChannelMessages() {
      return this.getChannelMessages();
   }

   public String getUsername() {
      return username;
   }

   public HashSet<UUID> getIgnoreList() {
      return ignoreList;
   }
}
