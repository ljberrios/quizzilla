package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.network.Session;

public interface Command {

    String getLabel();

    void execute(Session session, String[] args);

}
