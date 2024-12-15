package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketChatMessage;

import java.util.Arrays;

import static edu.uprb.quizzilla.util.Colors.*;

public class ChatMessageHandler implements PacketHandler<PacketChatMessage> {
    @Override
    public void handle(PacketChatMessage packet, Session session) {
        String sender = packet.getSender();
        Arrays.stream(packet.getMessages()).forEach(msg -> {
            if (sender != null)
                print(GREEN + "%s" + WHITE + ": %s", packet.getSender(), msg);
            else
                print(msg);
        });
    }
}
