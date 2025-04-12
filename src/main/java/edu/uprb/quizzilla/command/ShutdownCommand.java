package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.Client;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketSessionStop;

import static edu.uprb.quizzilla.util.Colors.*;

public class ShutdownCommand implements Command {

    private final Client main;

    public ShutdownCommand(Client main) {
        this.main = main;
    }

    @Override
    public String getLabel() {
        return "shutdown";
    }

    @Override
    public void execute(Session session, String[] args) {
        print(GREEN + "Stopping client and server...");
        if (session != null && session.isAlive())
            session.sendPacket(new PacketSessionStop());

        main.stop();
    }
}
