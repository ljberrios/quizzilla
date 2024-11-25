package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.network.Packet;

public class PacketChatMessage implements Packet {

    private final String sender;
    private final String message;

    public PacketChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
