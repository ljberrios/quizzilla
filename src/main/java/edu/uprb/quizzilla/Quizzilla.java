package edu.uprb.quizzilla;

import edu.uprb.quizzilla.client.KBInputListener;
import edu.uprb.quizzilla.command.*;
import edu.uprb.quizzilla.game.GameConfig;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.handlers.ChatMessageHandler;
import edu.uprb.quizzilla.network.handlers.PingHandler;
import edu.uprb.quizzilla.network.handlers.SessionStartHandler;
import edu.uprb.quizzilla.network.handlers.SessionStopHandler;
import edu.uprb.quizzilla.network.packets.PacketChatMessage;
import edu.uprb.quizzilla.network.packets.PacketPing;
import edu.uprb.quizzilla.network.packets.PacketSessionStart;
import edu.uprb.quizzilla.network.packets.PacketSessionStop;
import edu.uprb.quizzilla.server.SessionManager;

public class Quizzilla {

    private volatile boolean running = true;
    private Session clientSession;

    public static void main(String[] args) throws Exception {
        // start client
        Quizzilla main = new Quizzilla();

        // register packet handlers
        var dispatcher = new PacketDispatcher();
        dispatcher.registerHandler(PacketSessionStart.class, new SessionStartHandler());
        dispatcher.registerHandler(PacketSessionStop.class, new SessionStopHandler());
        dispatcher.registerHandler(PacketChatMessage.class, new ChatMessageHandler());
        dispatcher.registerHandler(PacketPing.class, new PingHandler());

        // load game settings and questions
        var config = new GameConfig();
        config.init();

        // create server without starting it
        var sessions = new SessionManager(config.getMaxSessions(), dispatcher);

        // register commands
        var commands = new CommandManager();
        commands.registerCommand(new CreateCommand(main, sessions, dispatcher));
        commands.registerCommand(new ShutdownCommand(main));
        commands.registerCommand(new JoinCommand(main, dispatcher));
        commands.registerCommand(new PingCommand());
        commands.registerCommand(new LeaveCommand(main));

        var inputListener = new KBInputListener(main, commands);
        var inputThread = inputListener.init();

        inputThread.join();
        sessions.shutdown();
    }

    public void stop() {
        closeClientSession();
        this.running = false;
    }

    public void closeClientSession() {
        if (clientSession.isAlive()) {
            clientSession.stop();
            clientSession = null;
        }
    }

    public boolean hasActiveClientSession() {
        return clientSession != null && clientSession.isAlive();
    }

    public boolean isRunning() {
        return running;
    }

    public Session getClientSession() {
        return clientSession;
    }

    public void setClientSession(Session clientSession) {
        this.clientSession = clientSession;
    }
}
