package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.client.ClientSession;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.server.SessionManager;
import static edu.uprb.quizzilla.util.Colors.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateCommand implements Command {

    private static final Logger logger = Logger.getLogger(CreateCommand.class.getName());

    private final SessionManager sessions;
    private final PacketDispatcher dispatcher;

    public CreateCommand(SessionManager sessions, PacketDispatcher dispatcher) {
        this.sessions = sessions;
        this.dispatcher = dispatcher;
    }

    @Override
    public String getLabel() {
        return "create";
    }

    @Override
    public void execute(Session session, String[] args) {
        try {
            sessions.init(25565);
            var serverAddress = InetAddress.getLocalHost();
            var client = new ClientSession(new Socket(serverAddress, 25565), dispatcher);
            Thread.startVirtualThread(client);
            print(GREEN + "Server started successfully");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while starting server", e);
            print(RED + "Error while starting server");
        }
    }
}
