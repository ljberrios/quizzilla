package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.network.Packet;

import java.util.UUID;

public class PacketPlayerAnswer implements Packet {

    private final UUID playerID;
    private final String answer;

    public PacketPlayerAnswer(UUID playerID, String answer) {
        this.playerID = playerID;
        this.answer = answer;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public String getAnswer() {
        return answer;
    }
}
