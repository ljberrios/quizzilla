package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.Client;
import edu.uprb.quizzilla.ClientSession;
import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketSessionStart;

import static edu.uprb.quizzilla.util.Colors.*;

public class SessionStartHandler implements PacketHandler<PacketSessionStart> {
    @Override
    public void handle(PacketSessionStart packet, Session session) {
        if (session instanceof ClientSession client)
            client.setID(packet.getSessionID());

        print(GREEN + "Joined server: " + CYAN + "%s", packet.getServerAddress());
    }
}