package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketSessionStop;

public class SessionStopHandler implements PacketHandler<PacketSessionStop> {
    @Override
    public void handle(PacketSessionStop packet, Session session) {
        session.stop();
    }
}
