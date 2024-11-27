package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketPing;

import static edu.uprb.quizzilla.util.Colors.*;

public class PingCommand implements Command {

    @Override
    public String getLabel() {
        return "ping";
    }

    @Override
    public void execute(Session session, String[] args) {
        if (session != null)
            session.sendPacket(new PacketPing());
        else
            print(RED + "Not connected to a server");
    }
}
