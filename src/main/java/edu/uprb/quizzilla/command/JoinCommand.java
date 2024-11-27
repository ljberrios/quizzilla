package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.Quizzilla;
import edu.uprb.quizzilla.client.ClientSession;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.Session;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.uprb.quizzilla.util.Colors.*;

public class JoinCommand implements Command {

    private static final Logger logger = Logger.getLogger(JoinCommand.class.getName());

    private final Quizzilla main;
    private final PacketDispatcher dispatcher;

    public JoinCommand(Quizzilla main, PacketDispatcher dispatcher) {
        this.main = main;
        this.dispatcher = dispatcher;
    }

    @Override
    public String getLabel() {
        return "join";
    }

    @Override
    public void execute(Session session, String[] args) {
        if (args.length == 1) {
            if (main.hasActiveClientSession()) {
                print(RED + "Already connected to a server");
                return;
            }

            Thread.startVirtualThread(() -> {
                try {
                    print(GREEN + "Attempting to connect to server...");
                    var socket = new Socket();
                    socket.connect(new InetSocketAddress(args[0], 25565), 5000);

                    var client = new ClientSession(socket, dispatcher);
                    var clientThread = Thread.startVirtualThread(client);

                    if (clientThread.join(Duration.ofMillis(3000)))
                        throw new ConnectException();
                    else
                        main.setClientSession(client);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Failed to connect to server", e);
                    print(RED + "Failed to join server");
                }
            });
        } else {
            print(RED + "Correct usage: /join <server ip>");
        }
    }
}
