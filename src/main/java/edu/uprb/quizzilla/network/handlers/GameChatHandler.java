package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.Client;
import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketGameChat;

import static edu.uprb.quizzilla.util.Colors.GREEN;
import static edu.uprb.quizzilla.util.Colors.WHITE;

public class GameChatHandler implements PacketHandler<PacketGameChat> {

    private final Client client;

    public GameChatHandler(Client client) {
        this.client = client;
    }

    @Override
    public void handle(PacketGameChat packet, Session session) {
        var game = client.getHostedGame();
        var player = game.getPlayer(packet.getSender());

        client.getHostedGame().broadcastf(
                GREEN + "%s: " + WHITE + "%s",
                player.getUsername(), packet.getMessage());
    }
}
