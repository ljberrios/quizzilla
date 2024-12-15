package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.Client;
import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketSessionStop;

public class SessionStopHandler implements PacketHandler<PacketSessionStop> {

    private final Client client;

    public SessionStopHandler(Client client) {
        this.client = client;
    }

    @Override
    public void handle(PacketSessionStop packet, Session session) {
        var game = client.getHostedGame();
        if (game != null)
            game.leave(session.getID());

        session.stop();
    }
}
