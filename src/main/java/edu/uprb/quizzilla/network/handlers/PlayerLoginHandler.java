package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketPlayerJoin;
import edu.uprb.quizzilla.network.packets.PacketPlayerLogin;

public class PlayerLoginHandler implements PacketHandler<PacketPlayerLogin> {
    @Override
    public void handle(PacketPlayerLogin packet, Session session) {
        session.sendPacket(new PacketPlayerJoin(session.getSocket().getLocalAddress()));
    }
}