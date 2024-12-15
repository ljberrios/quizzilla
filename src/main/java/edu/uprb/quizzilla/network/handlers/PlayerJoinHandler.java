package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.Client;
import edu.uprb.quizzilla.ServerSession;
import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketPlayerJoin;

public class PlayerJoinHandler implements PacketHandler<PacketPlayerJoin> {

    private final Client client;

    public PlayerJoinHandler(Client client) {
        this.client = client;
    }

    @Override
    public void handle(PacketPlayerJoin packet, Session session) {
        client.getHostedGame().join((ServerSession) session, packet.getUsername());
    }
}
