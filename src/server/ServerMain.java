package server;

import models.User;
import server.serverApp.ServerApp;

import java.util.concurrent.ConcurrentSkipListSet;

public class ServerMain {
   public static void main(String[] args) {
      new ServerApp().run();

//      System.out.println(testID());

   }

//   public static boolean testID() {
//      boolean value = true;
//      for (int i = 0; i < 1000000; i++) {
//         User user1 = new User("");
//         User user2 = new User("");
//         User user3 = new User("");
//         User user4 = new User("");
//         User user5 = new User("");
//         User user6 = new User("");
//         User user7 = new User("");
//         User user8 = new User("");
//         User user9 = new User("");
//         User user10 = new User("");
//         User user11 = new User("");
//         User user12 = new User("");
//         User user13 = new User("");
//         User user14 = new User("");
//         User user15 = new User("");
//         User user16 = new User("");
//         User user17 = new User("");
//         User user18 = new User("");
//         User user19 = new User("");
//         User user20 = new User("");
//         User user21 = new User("");
//         User user22 = new User("");
//         User user23 = new User("");
//         User user24 = new User("");
//         User user25 = new User("");
//         User user26 = new User("");
//         User user27 = new User("");
//         User user28 = new User("");
//         User user29 = new User("");
//         User user30 = new User("");
//
//         ConcurrentSkipListSet<User> set = new ConcurrentSkipListSet<>();
//         set.add(user1);
//         set.add(user2);
//         set.add(user3);
//         set.add(user4);
//         set.add(user5);
//         set.add(user6);
//         set.add(user7);
//         set.add(user8);
//         set.add(user9);
//         set.add(user10);
//         set.add(user11);
//         set.add(user12);
//         set.add(user13);
//         set.add(user14);
//         set.add(user15);
//         set.add(user16);
//         set.add(user17);
//         set.add(user18);
//         set.add(user19);
//         set.add(user20);
//         set.add(user21);
//         set.add(user22);
//         set.add(user23);
//         set.add(user24);
//         set.add(user25);
//         set.add(user26);
//         set.add(user27);
//         set.add(user28);
//         set.add(user29);
//         set.add(user30);
//
//         if (set.size() != 30) {
//            value = false;
//         }
//      }
//      return value;
//   }
}
