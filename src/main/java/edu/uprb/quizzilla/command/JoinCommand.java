package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.network.Session;

public class JoinCommand implements Command {

    @Override
    public String getLabel() {
        return "join";
    }

    @Override
    public void execute(Session session, String[] args) {

    }
}
