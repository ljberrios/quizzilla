package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.Client;
import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketPlayerReady;

public class PlayerReadyHandler implements PacketHandler<PacketPlayerReady> {

    private final Client client;

    public PlayerReadyHandler(Client client) {
        this.client = client;
    }

    @Override
    public void handle(PacketPlayerReady packet, Session session) {
        client.getHostedGame().ready(packet.getPlayerID());
    }
}
