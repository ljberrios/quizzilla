package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.network.Packet;

import java.util.UUID;

public class PacketPlayerReady implements Packet {

    private final UUID playerID;

    public PacketPlayerReady(UUID playerID) {
        this.playerID = playerID;
    }

    public UUID getPlayerID() {
        return playerID;
    }
}
