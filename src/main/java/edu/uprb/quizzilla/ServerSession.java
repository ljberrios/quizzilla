package edu.uprb.quizzilla;

import edu.uprb.quizzilla.network.Packet;
import edu.uprb.quizzilla.network.PacketDispatcher;
import edu.uprb.quizzilla.network.Session;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a {@link Session} with a connection from server to client.
 * That is, packets sent will be directed to the client.
 */
public class ServerSession implements Session {

    private static final Logger logger = Logger.getLogger(ServerSession.class.getName());

    private final BlockingQueue<Packet> outboundPackets = new LinkedBlockingQueue<>();

    private final Socket clientSocket;
    private final PacketDispatcher dispatcher;
    private final UUID id = UUID.randomUUID();
    private final List<Consumer<ServerSession>> onStopCallbacks = new ArrayList<>();
    private final AtomicBoolean alive = new AtomicBoolean(true);

    public ServerSession(Socket clientSocket, PacketDispatcher dispatcher) {
        this.clientSocket = clientSocket;
        this.dispatcher = dispatcher;
    }

    @Override
    public UUID getID() {
        return id;
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
    public PacketDispatcher getDispatcher() {
        return dispatcher;
    }

    @Override
    public BlockingQueue<Packet> getOutboundPackets() {
        return outboundPackets;
    }

    @Override
    public boolean isAlive() {
        return alive.get();
    }

    @Override
    public void stop() {
        if (alive.compareAndSet(true, false))
            notifyOnStop();
    }

    public void onStop(Consumer<ServerSession> consumer) {
        onStopCallbacks.add(consumer);
    }

    private void notifyOnStop() {
        onStopCallbacks.forEach(consumer -> consumer.accept(this));
    }

}
