package edu.uprb.quizzilla.client;

import edu.uprb.quizzilla.Quizzilla;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.command.CommandManager;
import edu.uprb.quizzilla.command.UnknownCommandException;

import java.util.Scanner;
import java.util.regex.Pattern;

import static edu.uprb.quizzilla.util.Colors.*;

public class KBInputListener {

    private static final Pattern CMD_REGEX = Pattern.compile("^/(\\w+)(?:\\s+(.*))?$");

    private final Quizzilla main;
    private final CommandManager commands;

    public KBInputListener(Quizzilla main, CommandManager commands) {
        this.main = main;
        this.commands = commands;
    }

    public Thread init() {
        return Thread.startVirtualThread(() -> {
            var scanner = new Scanner(System.in);
            while (main.isRunning())
                if (scanner.hasNextLine())
                    processInput(scanner.nextLine());
        });
    }

    private void processInput(String input) {
        var matcher = CMD_REGEX.matcher(input);
        if (matcher.matches()) {
            String label = matcher.group(1);
            String args = matcher.group(2);
            try {
                commands.processCommand(main.getClientSession(), label,
                        args != null ? args.split(" ") : new String[]{});
            } catch (UnknownCommandException e) {
                print(RED + e.getMessage());
            }
        }
    }
}
