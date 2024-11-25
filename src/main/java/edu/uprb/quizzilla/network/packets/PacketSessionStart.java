package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.network.Packet;

import java.net.InetAddress;
import java.util.UUID;

public class PacketSessionStart implements Packet {

    private final InetAddress serverAddress;
    private final UUID uuid;

    public PacketSessionStart(InetAddress serverAddress, UUID uuid) {
        this.serverAddress = serverAddress;
        this.uuid = uuid;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public UUID getUuid() {
        return uuid;
    }
}
