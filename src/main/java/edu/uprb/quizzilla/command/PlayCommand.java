package edu.uprb.quizzilla.command;

import edu.uprb.quizzilla.Client;
import edu.uprb.quizzilla.ClientSession;
import edu.uprb.quizzilla.game.Game;
import edu.uprb.quizzilla.game.GameConfig;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.Server;
import static edu.uprb.quizzilla.util.Colors.*;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayCommand implements Command {

    private static final Logger logger = Logger.getLogger(PlayCommand.class.getName());

    private final Client client;
    private final Server server;
    private final PacketDispatcher dispatcher;
    private final GameConfig config;

    public PlayCommand(Client client,
                       Server server, PacketDispatcher dispatcher, GameConfig config)
    {
        this.client = client;
        this.server = server;
        this.dispatcher = dispatcher;
        this.config = config;
    }

    @Override
    public String getLabel() {
        return "play";
    }

    @Override
    public void execute(Session session, String[] args) {
        if (args.length != 1) {
            print(RED + "Correct usage: /play <username>");
            return;
        } else if (client.getHostedGame() != null) {
            print(RED + "Game already created");
            return;
        }

        try {
            // start server
            server.listenForConnections(25565);

            // join server
            var serverAddress = InetAddress.getLocalHost();
            var clientSession = new ClientSession(
                    new Socket(serverAddress, 25565), dispatcher);
            var clientThread = Thread.startVirtualThread(clientSession);
            client.setClientSession(clientSession);

            print(GREEN + "Server started successfully");

            if (clientThread.join(Duration.ofMillis(3000))) {
                throw new ConnectException();
            } else {
                // client was able to join server, create game
                var hostSession = server.getHost();
                var game = new Game(hostSession.getID(), config);
                game.join(hostSession, args[0]);
                client.setHostedGame(game);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error while starting server", e);
            print(RED + "Error while starting server");
        }
    }
}
