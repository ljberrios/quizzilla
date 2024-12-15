package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.network.Packet;

import java.util.UUID;

public class PacketPlayerJoin implements Packet {

    private final UUID sessionID;
    private final String username;

    public PacketPlayerJoin(UUID sessionID, String username) {
        this.sessionID = sessionID;
        this.username = username;
    }

    public UUID getSessionID() {
        return sessionID;
    }

    public String getUsername() {
        return username;
    }
}
