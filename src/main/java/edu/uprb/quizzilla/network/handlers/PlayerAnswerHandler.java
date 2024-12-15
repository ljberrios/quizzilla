package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.Client;
import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketPlayerAnswer;

public class PlayerAnswerHandler implements PacketHandler<PacketPlayerAnswer> {

    private final Client client;

    public PlayerAnswerHandler(Client client) {
        this.client = client;
    }

    @Override
    public void handle(PacketPlayerAnswer packet, Session session) {
        client.getHostedGame().answer(packet.getPlayerID(), packet.getAnswer());
    }
}
