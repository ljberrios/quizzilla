package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.Client;
import edu.uprb.quizzilla.ClientSession;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketPlayerJoin;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

import static edu.uprb.quizzilla.util.Colors.*;

public class JoinCommand implements Command {

    private static final Logger logger = Logger.getLogger(JoinCommand.class.getName());

    private final Client client;
    private final PacketDispatcher dispatcher;

    public JoinCommand(Client client, PacketDispatcher dispatcher) {
        this.client = client;
        this.dispatcher = dispatcher;
    }

    @Override
    public String getLabel() {
        return "join";
    }

    @Override
    public void execute(Session session, String[] args) {
        if (args.length != 2) {
            print(RED + "Correct usage: /join <server ip> <username>");
        } else if (client.hasActiveSession()) {
            print(RED + "Already connected to a server");
        } else {
            Thread.startVirtualThread(() -> {
                try {
                    print(GREEN + "Attempting to connect to server...");
                    var socket = new Socket();
                    socket.connect(new InetSocketAddress(args[0], 25565), 5000);

                    var client = new ClientSession(socket, dispatcher);
                    var clientThread = Thread.startVirtualThread(client);

                    if (clientThread.join(Duration.ofMillis(3000)))
                        throw new ConnectException();

                    this.client.setClientSession(client);
                    client.sendPacket(new PacketPlayerJoin(client.getID(), args[1]));
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Failed to connect to server", e);
                    print(RED + "Failed to join server");
                }
            });

        }
    }
}
