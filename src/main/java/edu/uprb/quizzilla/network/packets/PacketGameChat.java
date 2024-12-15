package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.network.Packet;

import java.util.UUID;

public class PacketGameChat implements Packet {

    private final UUID sender;
    private final String message;

    public PacketGameChat(UUID sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public UUID getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
