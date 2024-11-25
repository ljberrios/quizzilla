package edu.uprb.quizzilla.server;

import edu.uprb.quizzilla.network.Packet;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.Session;

import java.net.Socket;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Represents a {@link Session} with a connection from server to client.
 * That is, packets sent will be directed to the client.
 */
public class ServerSession implements Session {

    private static final Logger logger = Logger.getLogger(ServerSession.class.getName());

    private final Queue<Packet> outboundPackets = new ConcurrentLinkedQueue<>();

    private final Socket clientSocket;
    private final PacketDispatcher dispatcher;
    private final UUID id = UUID.randomUUID();
    private volatile boolean alive = true;

    public ServerSession(Socket clientSocket, PacketDispatcher dispatcher) {
        this.clientSocket = clientSocket;
        this.dispatcher = dispatcher;
    }

    @Override
    public Socket getSocket() {
        return clientSocket;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public UUID getID() {
        return id;
    }

    @Override
    public PacketDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public Queue<Packet> getOutboundPackets() {
        return outboundPackets;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void stop() {
        logger.info("Session ended: " + clientSocket.getInetAddress());
        alive = false;
    }

}
