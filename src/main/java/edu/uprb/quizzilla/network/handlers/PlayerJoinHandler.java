package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketPlayerJoin;

public class PlayerJoinHandler implements PacketHandler<PacketPlayerJoin> {
    @Override
    public void handle(PacketPlayerJoin packet, Session session) {
        session.getLogger().info("Joined server: " + packet.getServerAddress());
    }
}