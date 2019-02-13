package server.serverApp;

import models.Channel;
import models.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingDeque;

public class ActiveChannelController {
   private static ActiveChannelController ourInstance = new ActiveChannelController();

   public static ActiveChannelController getInstance() {
      return ourInstance;
   }

   private ConcurrentSkipListSet<Channel> channels;

   private ActiveChannelController() {
      this.channels = new ConcurrentSkipListSet<>();
   }


   public Channel getChannel(UUID ID) {
      for (Channel c : this.channels) {
         if (c.getID().equals(ID)) {
            return c;
         }
      }
      return null;
   }

   // TODO: 2019-02-13 method for adding new channels
   // TODO: 2019-02-13 method for deleting channels 

}
