package edu.uprb.quizzilla;

import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.packets.PacketSessionStart;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains all {@link ServerSession}s and their respective virtual threads.
 * To start the server, {@link #listenForConnections(int)} must be called.
 */
public class Server {

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private final ExecutorService sessionThreadPool = Executors.newVirtualThreadPerTaskExecutor();
    private final Map<UUID, ServerSession> sessions = new ConcurrentHashMap<>();
    private final Set<String> addresses = new HashSet<>();
    private final int maxSessions;
    private final PacketDispatcher dispatcher;

    private ServerSession host;

    public Server(int maxSessions, PacketDispatcher dispatcher) {
        this.maxSessions = maxSessions;
        this.dispatcher = dispatcher;
    }

    public void listenForConnections(int port) {
        Thread.startVirtualThread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                logger.info("Server is listening on port " + port);
                while (sessions.size() < maxSessions)
                    startSession(serverSocket);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error with server communication", e);
            }
        });
    }

    private void startSession(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept();
        if (addresses.contains(socket.getInetAddress().getHostAddress())) {
            socket.close();
            return;
        }

        ServerSession session = new ServerSession(socket, dispatcher);
        // Assign server leader if first player
        if (this.host == null) {
            this.host = session;
            logger.info("Server Host: " + socket.getInetAddress());
        }

        // Start the session
        sessions.put(session.getID(), session);
        addresses.add(socket.getInetAddress().getHostAddress());

        // Add session close callback
        session.onStop(this::stopSession);

        // Handle the session in a new thread
        sessionThreadPool.submit(session);

        // Send session start packet to client
        session.sendPacket(new PacketSessionStart(
                socket.getLocalAddress(), session.getID()));

        logger.info(String.format(
                "New session started: %s | Sessions: %d/%d\n",
                socket.getInetAddress(), sessions.size(), maxSessions)
        );
    }

    public void stopSession(UUID uuid) {
        ServerSession session = sessions.get(uuid);
        if (session != null)
            stopSession(session);
    }

    public void stopSession(ServerSession session) {
        if (session.isAlive())
            session.stop();

        var socket = session.getSocket();
        addresses.remove(socket.getInetAddress().getHostAddress());
        sessions.remove(session.getID());
        logger.info(String.format(
                "Session ended: %s | Sessions: %d/%d\n",
                socket.getInetAddress(), sessions.size(), maxSessions));
    }

    public void shutdown() {
        if (!sessionThreadPool.isShutdown()) {
            logger.info("Shutting down server. Ending all sessions...");
            sessions.keySet().forEach(this::stopSession);
            sessionThreadPool.shutdown();
        }
    }

    public ServerSession getSession(UUID uuid) {
        return sessions.get(uuid);
    }

    public Map<UUID, ServerSession> getSessions() {
        return sessions;
    }

    public ServerSession getHost() {
        return host;
    }
}
