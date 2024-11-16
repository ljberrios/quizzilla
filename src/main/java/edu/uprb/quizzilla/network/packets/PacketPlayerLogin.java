package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.network.Packet;

import java.net.InetAddress;

public class PacketPlayerLogin implements Packet {

    private final String username;
    private final InetAddress clientAddress;

    public PacketPlayerLogin(String username, InetAddress clientAddress) {
        this.username = username;
        this.clientAddress = clientAddress;
    }

    public String getUsername() {
        return username;
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }
}
