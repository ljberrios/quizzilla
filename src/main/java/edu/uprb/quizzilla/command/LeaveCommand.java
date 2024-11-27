package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.Quizzilla;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketSessionStop;

import static edu.uprb.quizzilla.util.Colors.*;

public class LeaveCommand implements Command {

    private final Quizzilla main;

    public LeaveCommand(Quizzilla main) {
        this.main = main;
    }

    @Override
    public String getLabel() {
        return "leave";
    }

    @Override
    public void execute(Session session, String[] args) {
        if (main.hasActiveClientSession()) {
            session.sendPacket(new PacketSessionStop());
            main.closeClientSession();
            print(GREEN + "Left the server");
        } else {
            print(RED + "Not connected to a server");
        }
    }
}
