package server.serverApp;

import models.Channel;
import models.User;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class ActiveChannelController {
   private static ActiveChannelController ourInstance = new ActiveChannelController();

   public static ActiveChannelController getInstance() {
      return ourInstance;
   }


   // TODO: 2019-02-14 consider changing set to treemap with UUID as key and channel som value
   private ConcurrentSkipListSet<Channel> channels;

   private ActiveChannelController() {
      this.channels = new ConcurrentSkipListSet<>();
   }


   public Channel getChannel(String name) {
      for (Channel c : this.channels) {
         if (c.getName().equals(name)) {
            return c;
         }
      }
      return null;
   }


   public boolean addChannel(String name) {
      return channels.add(new Channel(name));
   }

   public boolean removeChannel(String name) {

      var n = new Object() {
         boolean value = false;
      };

      this.channels = channels.stream()
              .filter(c -> {
                 if (!c.getName().equals(name)) {
                    return true;
                 }
                 n.value = true;
                 return false;
              })
              .collect(Collectors.toCollection(ConcurrentSkipListSet::new));

      return n.value;
   }

   public void removeUserFromChannels(User user) {
      this.channels.forEach(c -> c.removeUser(user));
   }

   public void addUserToChannel(User user, String name) {
      Channel channel = null;
      try {
         channel = this.channels.stream()
                 .filter(c -> c.getName().equals(name))
                 .toArray(Channel[]::new)[0];
      } catch (ArrayIndexOutOfBoundsException e) {
      }

      if (channel == null) {
         this.addChannel(name);
         this.addUserToChannel(user, name);
      } else {
         this.addUserToChannel(user, channel.getName());
      }

   }


}
