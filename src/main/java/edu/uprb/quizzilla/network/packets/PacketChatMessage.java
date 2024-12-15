package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.network.Packet;

public class PacketChatMessage implements Packet {

    private final String sender;
    private final String[] messages;

    public PacketChatMessage(String sender, String... messages) {
        this.sender = sender;
        this.messages = messages;
    }

    public String getSender() {
        return sender;
    }

    public String[] getMessages() {
        return messages;
    }
}
