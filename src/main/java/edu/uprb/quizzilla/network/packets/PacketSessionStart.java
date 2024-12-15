package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.network.Packet;

import java.net.InetAddress;
import java.util.UUID;

public class PacketSessionStart implements Packet {

    private final InetAddress serverAddress;
    private final UUID sessionID;

    public PacketSessionStart(InetAddress serverAddress, UUID sessionID) {
        this.serverAddress = serverAddress;
        this.sessionID = sessionID;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public UUID getSessionID() {
        return sessionID;
    }
}
