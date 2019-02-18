package models;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable, Sendable {
    private final MessageType type;
    private UUID sender;
    private final UUID receiver;
    private final String nickname;
    private final String textContent;
    private final String channel;

    private Message(MessageBuilder messageBuilder) {
        this.type = messageBuilder.type;
        this.sender = messageBuilder.sender;
        this.receiver = messageBuilder.receiver;
        this.nickname = messageBuilder.nickname;
        this.textContent = messageBuilder.textContent;
        this.channel = messageBuilder.channel;
    }

    public MessageType getType() {
        return type;
    }

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "I am a message that was sent as an object :)";
    }
    public static class MessageBuilder {
        private final MessageType type;
        private UUID sender;
        private UUID receiver;
        private String nickname;
        private String textContent;
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
            this.textContent = content;
            return this;
        }

        public MessageBuilder toChannel(String channel) {
            this.channel = channel;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
