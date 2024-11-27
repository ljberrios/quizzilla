package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.client.ClientSession;
import edu.uprb.quizzilla.client.KBInputListener;
import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketSessionStart;

import static edu.uprb.quizzilla.util.Colors.*;

public class SessionStartHandler implements PacketHandler<PacketSessionStart> {

    @Override
    public void handle(PacketSessionStart packet, Session session) {
        print(GREEN + "Joined server: " + CYAN + "%s", packet.getServerAddress());
    }
}