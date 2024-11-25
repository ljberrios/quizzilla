package edu.uprb.quizzilla;

import edu.uprb.quizzilla.client.KBInputListener;
import edu.uprb.quizzilla.command.ExitCommand;
import edu.uprb.quizzilla.game.GameConfig;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.handlers.ChatMessageHandler;
import edu.uprb.quizzilla.network.handlers.SessionStartHandler;
import edu.uprb.quizzilla.network.packets.PacketChatMessage;
import edu.uprb.quizzilla.network.packets.PacketSessionStart;
import edu.uprb.quizzilla.server.SessionManager;
import edu.uprb.quizzilla.command.CommandManager;
import edu.uprb.quizzilla.command.CreateCommand;

public class Quizzilla {

    private volatile boolean running = true;

    public static void main(String[] args) throws Exception {
        // start client
        Quizzilla main = new Quizzilla();

        // register packet handlers
        var dispatcher = new PacketDispatcher();
        dispatcher.registerHandler(PacketSessionStart.class, new SessionStartHandler());
        dispatcher.registerHandler(PacketChatMessage.class, new ChatMessageHandler());

        // load game settings and questions
        var config = new GameConfig();
        config.init();

        // create server without starting it
        var sessions = new SessionManager(config.getMaxSessions(), dispatcher);

        // register commands
        var commands = new CommandManager();
        commands.registerCommand(new CreateCommand(sessions, dispatcher));
        commands.registerCommand(new ExitCommand(main));

        var inputListener = new KBInputListener(main, commands);
        var inputThread = inputListener.init();

        inputThread.join();
        sessions.shutdown();
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        this.running = false;
    }
}
