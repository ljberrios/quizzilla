package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.network.Packet;

import java.net.InetAddress;

public class PacketPlayerJoin implements Packet {

    private final InetAddress serverAddress;

    public PacketPlayerJoin(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }
}
