package models;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable, Sendable{

   public static class MessageBuilder {


      private UUID sender;
      private UUID receiver;
      private String nickname;
      private MessageType type;
      private String TextContent;
      private String channel;

      public MessageBuilder(MessageType type) {
         this.type = type;
      }

      public MessageBuilder fromSender(UUID sender) {
         this.sender = sender;
         return this;
      }

      public MessageBuilder toReceiver(UUID receiver) {
         this.receiver = receiver;
         return this;
      }

      public MessageBuilder nickname(String nickname) {
         this.nickname = nickname;
         return this;
      }

      public MessageBuilder withContent(String content) {
         this.TextContent = content;
         return this;
      }


//      public Message() {
//         this.SENDER = null;
//         this.RECEIVER = null;
//         this.NICKNAME = null;
//         this.TYPE = MessageType.CHANNEL_MESSAGE;
//         this.TEXT_CONTENT = null;
//         this.CHANNEL = null;
//      }
//
//      public Message(MessageType type) {
//         this.SENDER = null;
//         this.RECEIVER = null;
//         this.NICKNAME = null;
//         this.TYPE = type;
//         this.TEXT_CONTENT = null;
//         this.CHANNEL = null;
//      }

      @Override
      public String toString() {
         return "I am a message that was sent as an object :)";
      }
   }
}
