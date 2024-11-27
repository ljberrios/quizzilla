package edu.uprb.quizzilla.client;

import edu.uprb.quizzilla.network.Packet;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.Session;

import java.net.Socket;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Represents a {@link Session} with a connection from client to server.
 * That is, packets sent will be directed to the server.
 */
public class ClientSession implements Session {

    private static final Logger logger = Logger.getLogger(ClientSession.class.getName());

    private final BlockingQueue<Packet> outboundPackets = new LinkedBlockingQueue<>();

    private final Socket serverSocket;
    private final PacketDispatcher dispatcher;
    private volatile boolean alive = true;

    public ClientSession(Socket serverSocket, PacketDispatcher dispatcher) {
        this.serverSocket = serverSocket;
        this.dispatcher = dispatcher;
    }

    @Override
    public Socket getSocket() {
        return serverSocket;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public PacketDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public BlockingQueue<Packet> getOutboundPackets() {
        return outboundPackets;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void stop() {
        if (alive) {
            logger.info("Session ended: " + serverSocket.getInetAddress());
            alive = false;
        }
    }
}
