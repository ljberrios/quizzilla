package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.Quizzilla;
import edu.uprb.quizzilla.network.Session;

import static edu.uprb.quizzilla.util.Colors.*;

public class ShutdownCommand implements Command {

    private final Quizzilla main;

    public ShutdownCommand(Quizzilla main) {
        this.main = main;
    }

    @Override
    public String getLabel() {
        return "shutdown";
    }

    @Override
    public void execute(Session session, String[] args) {
        print(GREEN + "Stopping client and server...");
        main.stop();
    }
}
