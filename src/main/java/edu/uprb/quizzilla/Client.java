package edu.uprb.quizzilla;

import edu.uprb.quizzilla.command.*;
import edu.uprb.quizzilla.game.Game;
import edu.uprb.quizzilla.game.GameConfig;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.handlers.*;
import edu.uprb.quizzilla.network.packets.*;

public class Client {

    private ClientSession clientSession;
    private Thread inputThread;
    private Game hostedGame;

    public static void main(String[] args) throws Exception {
        // start client
        var client = new Client();

        // register packet handlers
        var dispatcher = new PacketDispatcher();
        dispatcher.registerHandler(PacketSessionStart.class, new SessionStartHandler());
        dispatcher.registerHandler(PacketSessionStop.class, new SessionStopHandler(client));
        dispatcher.registerHandler(PacketChatMessage.class, new ChatMessageHandler());
        dispatcher.registerHandler(PacketPing.class, new PingHandler());
        dispatcher.registerHandler(PacketPlayerReady.class, new PlayerReadyHandler(client));
        dispatcher.registerHandler(PacketPlayerAnswer.class, new PlayerAnswerHandler(client));
        dispatcher.registerHandler(PacketPlayerJoin.class, new PlayerJoinHandler(client));
        dispatcher.registerHandler(PacketPlaySound.class, new PlaySoundHandler());
        dispatcher.registerHandler(PacketGameChat.class, new GameChatHandler(client));

        // load game settings and questions
        var config = new GameConfig();
        config.init();

        // create server without starting it
        var server = new Server(config.getMaxSessions(), dispatcher);

        // register commands
        var commands = new CommandManager();
        commands.registerCommand(new PlayCommand(client, server, dispatcher, config));
        commands.registerCommand(new ShutdownCommand(client));
        commands.registerCommand(new JoinCommand(client, dispatcher));
        commands.registerCommand(new PingCommand());
        commands.registerCommand(new LeaveCommand(client));
        commands.registerCommand(new AnswerCommand());
        commands.registerCommand(new ReadyCommand());
        commands.registerCommand(new ChatCommand());

        var inputListener = new CommandListener(client, commands);
        var inputThread = inputListener.init();
        client.setInputThread(inputThread);

        inputThread.join();
        server.shutdown();
    }

    public void stop() {
        if (hostedGame != null)
            hostedGame.end();

        closeSession();
        inputThread.interrupt();
    }

    public void closeSession() {
        if (clientSession.isAlive())
            clientSession.stop();
    }

    public boolean hasActiveSession() {
        return clientSession != null && clientSession.isAlive();
    }

    public ClientSession getClientSession() {
        return clientSession;
    }

    public void setClientSession(ClientSession clientSession) {
        this.clientSession = clientSession;
    }

    public void setInputThread(Thread inputThread) {
        this.inputThread = inputThread;
    }

    public Game getHostedGame() {
        return hostedGame;
    }

    public void setHostedGame(Game hostedGame) {
        this.hostedGame = hostedGame;
    }
}
