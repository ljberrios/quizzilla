package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketPlayerReady;

import static edu.uprb.quizzilla.util.Colors.*;

public class ReadyCommand implements Command {

    @Override
    public String getLabel() {
        return "ready";
    }

    @Override
    public void execute(Session session, String[] args) {
        if (session != null && session.isAlive())
            session.sendPacket(new PacketPlayerReady(session.getID()));
        else
            print(RED + "Not connected to a server");
    }
}
