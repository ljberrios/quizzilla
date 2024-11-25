package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.network.Session;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();

    public void registerCommand(Command command) {
        commands.put(command.getLabel(), command);
    }

    public void processCommand(Session session, String label, String[] args)
            throws UnknownCommandException
    {
        Command cmd = commands.get(label);
        if (cmd == null)
            throw new UnknownCommandException(label);

        cmd.execute(session, args);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
