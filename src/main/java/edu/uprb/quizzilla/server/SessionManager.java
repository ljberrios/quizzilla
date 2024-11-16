package edu.uprb.quizzilla.server;

import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.packets.PacketPlayerJoin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class SessionManager {

    public static final int MAX_SESSIONS = 10;

    private static final Logger logger = Logger.getLogger(SessionManager.class.getName());

    private final ExecutorService sessionThreadPool = Executors.newVirtualThreadPerTaskExecutor();
    private final Map<InetAddress, ServerSession> sessions = new ConcurrentHashMap<>();
    private final PacketDispatcher dispatcher;

    private ServerSession leader;

    public SessionManager(PacketDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void listen(int port) {
        Thread.startVirtualThread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                logger.info("Server is listening on port " + port);
                while (sessions.size() < MAX_SESSIONS)
                    startSession(serverSocket);
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error with server communication", e);
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
        sessions.put(socket.getInetAddress(), session);
        // Handle the session in a new thread
        sessionThreadPool.submit(session);

        // send successful join packet to client
        session.sendPacket(new PacketPlayerJoin(socket.getLocalAddress()));

        logger.info(String.format(
                "New session started: %s | Sessions: %d/%d\n",
                socket.getInetAddress(), sessions.size(), MAX_SESSIONS)
        );
    }

    public void stopSession(InetAddress clientAddress) {
        ServerSession session = sessions.get(clientAddress);
        if (session != null) {
            session.stop();
            sessions.remove(clientAddress);
        }
    }

    public void shutdown() {
        if (!sessionThreadPool.isShutdown()) {
            logger.info("Shutting down. Ending all sessions...");
            sessions.values().forEach(ServerSession::stop);
            sessionThreadPool.shutdown();
        }
    }

    public ServerSession getSession(InetAddress clientAddress) {
        return sessions.get(clientAddress);
    }

    public Map<InetAddress, ServerSession> getSessions() {
        return sessions;
    }

    public ServerSession getServerLeader() {
        return leader;
    }
}
