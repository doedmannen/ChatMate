package models;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

public class Message implements Serializable {

   public UUID SENDER;
   public UUID RECIVER;
   public MessageType TYPE;
   public String TEXT_CONTENT;
   public String CHANNEL;

   public Message() {
      this.SENDER = null;
      this.RECIVER = null;
      this.TYPE = MessageType.CHANNEL_MESSAGE;
      this.TEXT_CONTENT = null;
      this.CHANNEL = null;
   }

   public Message(MessageType type) {
      this.SENDER = null;
      this.RECIVER = null;
      this.TYPE = type;
      this.TEXT_CONTENT = null;
      this.CHANNEL = null;
   }

   @Override
   public String toString() {
      return "I am a message that was sent as an object :)";
   }
}
