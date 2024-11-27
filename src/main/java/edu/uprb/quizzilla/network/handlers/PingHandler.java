package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketChatMessage;
import edu.uprb.quizzilla.network.packets.PacketPing;

public class PingHandler implements PacketHandler<PacketPing> {

    @Override
    public void handle(PacketPing packet, Session session) {
        session.sendPacket(new PacketChatMessage("Server", "Pong"));
    }
}
