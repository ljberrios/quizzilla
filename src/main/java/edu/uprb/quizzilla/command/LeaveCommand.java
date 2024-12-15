package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.Client;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketSessionStop;

import static edu.uprb.quizzilla.util.Colors.*;

public class LeaveCommand implements Command {

    private final Client main;

    public LeaveCommand(Client main) {
        this.main = main;
    }

    @Override
    public String getLabel() {
        return "leave";
    }

    @Override
    public void execute(Session session, String[] args) {
        if (session != null && session.isAlive()) {
            session.sendPacket(new PacketSessionStop());
            main.closeSession();
            print(GREEN + "Left the server");
        } else {
            print(RED + "Not connected to a server");
        }
    }
}
