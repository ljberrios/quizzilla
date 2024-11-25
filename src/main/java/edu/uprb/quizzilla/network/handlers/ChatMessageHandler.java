package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketChatMessage;

import static edu.uprb.quizzilla.util.Colors.*;

public class ChatMessageHandler implements PacketHandler<PacketChatMessage> {
    @Override
    public void handle(PacketChatMessage packet, Session session) {
        // For now, print on terminal
        // Later, use GUI
        print(GREEN + "%s" + WHITE + ": %s",
                packet.getSender(), packet.getMessage());
    }
}
