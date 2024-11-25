package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.Quizzilla;
import edu.uprb.quizzilla.network.Session;

import static edu.uprb.quizzilla.util.Colors.*;

public class ExitCommand implements Command {

    private final Quizzilla main;

    public ExitCommand(Quizzilla main) {
        this.main = main;
    }

    @Override
    public String getLabel() {
        return "exit";
    }

    @Override
    public void execute(Session session, String[] args) {
        print(GREEN + "Stopping client and server...");
        main.stop();
    }
}
