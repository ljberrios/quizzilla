package edu.uprb.quizzilla.server;

import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.packets.PacketSessionStart;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contains all {@link ServerSession}s and their respective virtual threads.
 * To start the server, {@link #init(int)} must be called.
 */
public class SessionManager {

    private static final Logger logger = Logger.getLogger(SessionManager.class.getName());

    private final ExecutorService sessionThreadPool = Executors.newVirtualThreadPerTaskExecutor();
    private final Map<UUID, ServerSession> sessions = new ConcurrentHashMap<>();
    private final int maxSessions;
    private final PacketDispatcher dispatcher;

    private ServerSession leader;

    public SessionManager(int maxSessions, PacketDispatcher dispatcher) {
        this.maxSessions = maxSessions;
        this.dispatcher = dispatcher;
    }

    public void init(int port) {
        listenForConnections(port);
        startCleanupThread();
    }

    private void listenForConnections(int port) {
        // listen for incoming connections
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

    private void startCleanupThread() {
        // constantly clean up sessions with closed sockets
        Thread.startVirtualThread(() -> {
            while (!sessionThreadPool.isShutdown()) {
                try {
                    sessions.entrySet().removeIf(entry -> !entry.getValue().isAlive());
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.log(Level.WARNING, "Socket cleanup thread interrupted", e);
                }
            }
        });
    }

    private void startSession(ServerSocket serverSocket) throws IOException {
        Socket socket = serverSocket.accept();
        ServerSession session = new ServerSession(socket, dispatcher);
        // Assign server leader if first player
        if (this.leader == null) {
            this.leader = session;
            logger.info("Assigned server leader: " + socket.getInetAddress());
        }

        // Start the session
        sessions.put(session.getID(), session);
        // Handle the session in a new thread
        sessionThreadPool.submit(session);

        // send session start packet to client
        session.sendPacket(new PacketSessionStart(
                socket.getLocalAddress(), session.getID()));

        logger.info(String.format(
                "New session started: %s | Sessions: %d/%d\n",
                socket.getInetAddress(), sessions.size(), maxSessions)
        );
    }

    public void stopSession(UUID uuid) {
        ServerSession session = sessions.get(uuid);
        if (session != null) {
            session.stop();
            sessions.remove(uuid);
        }
    }

    public void shutdown() {
        if (!sessionThreadPool.isShutdown()) {
            logger.info("Shutting down. Ending all sessions...");
            sessions.values().forEach(ServerSession::stop);
            sessions.clear();
            sessionThreadPool.shutdown();
        }
    }

    public ServerSession getSession(UUID uuid) {
        return sessions.get(uuid);
    }

    public Map<UUID, ServerSession> getSessions() {
        return sessions;
    }

    public ServerSession getServerLeader() {
        return leader;
    }
}
