package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.Client;

import java.util.Scanner;
import java.util.regex.Pattern;

import static edu.uprb.quizzilla.util.Colors.*;

public class CommandListener {

    private static final Pattern CMD_REGEX = Pattern.compile("^/(\\w+)(?:\\s+(.*))?$");

    private final Client main;
    private final CommandManager commands;

    public CommandListener(Client main, CommandManager commands) {
        this.main = main;
        this.commands = commands;
    }

    public Thread init() {
        return Thread.startVirtualThread(() -> {
            var scanner = new Scanner(System.in);
            while (!Thread.currentThread().isInterrupted())
                if (scanner.hasNextLine())
                    processInput(scanner.nextLine());
        });
    }

    private void processInput(String input) {
        try {
            var matcher = CMD_REGEX.matcher(input.trim());
            var clientSession = main.getClientSession();
            if (matcher.matches()) {
                String label = matcher.group(1);
                String args = matcher.group(2);
                commands.processCommand(clientSession, label,
                        args != null ? args.split(" ") : new String[]{});
            } else {
                commands.processCommand(clientSession, "chat", input.trim().split(" "));
            }
        } catch (UnknownCommandException e) {
            print(RED + e.getMessage());
        }
    }
}
