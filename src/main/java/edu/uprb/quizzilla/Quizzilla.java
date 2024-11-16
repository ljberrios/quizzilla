package edu.uprb.quizzilla;

import edu.uprb.quizzilla.client.ClientSession;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.handlers.PlayerJoinHandler;
import edu.uprb.quizzilla.network.handlers.PlayerLoginHandler;
import edu.uprb.quizzilla.network.packets.PacketPlayerJoin;
import edu.uprb.quizzilla.network.packets.PacketPlayerLogin;
import edu.uprb.quizzilla.server.SessionManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Quizzilla {
    public static void main(String[] args) throws IOException, InterruptedException {
        PacketDispatcher dispatcher = new PacketDispatcher();
        // register packet handlers
        dispatcher.registerHandler(PacketPlayerLogin.class, new PlayerLoginHandler());
        dispatcher.registerHandler(PacketPlayerJoin.class, new PlayerJoinHandler());

        SessionManager sessions = new SessionManager(dispatcher);
        sessions.listen(8080);

        InetAddress serverAddress = InetAddress.getLocalHost();
        ClientSession session = new ClientSession(new Socket(serverAddress, 8080), dispatcher);
        Thread clientThread = Thread.startVirtualThread(session);
        clientThread.join();

        sessions.shutdown();
    }
}
