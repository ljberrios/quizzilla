package edu.uprb.quizzilla.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A session in networking is a temporary exchange of
 * information between two or more devices over a network.
 * Instances of the implementations of this interface are meant
 * to run in their own {@link Thread}. This is why it extends {@link Runnable}.
 */
public interface Session extends Runnable {

    /**
     * The {@link Socket} that facilitates communication with
     * the server/client.
     */
    Socket getSocket();

    /**
     * The {@link Logger} for class, used for logging all kinds
     * of information, warnings, etc.
     */
    Logger getLogger();

    /**
     * See {@link PacketDispatcher}.
     */
    PacketDispatcher getDispatcher();

    /**
     * Contains the packets that will get sent to the server/client.
     */
    Queue<Packet> getOutboundPackets();

    /**
     * True if the session hasn't ended yet.
     */
    boolean isAlive();

    /**
     * Queues the given packet to be sent to the server/client.
     *
     * @param packet the packet to send
     */
    default void sendPacket(Packet packet) {
        getOutboundPackets().add(packet);
    }

    /**
     * Handles the sending, receiving, and processing of {@link Packet}s. Also,
     * it handles the graceful closing of the {@link Socket}. The code in
     * this method is meant to run in a {@link Thread} different from
     * the main one.
     */
    @SuppressWarnings("unchecked")
    default void run() {
        Socket socket = getSocket();
        Logger logger = getLogger();
        try (var out = new ObjectOutputStream(socket.getOutputStream());
             var in = new ObjectInputStream(socket.getInputStream()))
        {
            while (isAlive()) {
                // send pending outbound packets
                Packet toSend;
                while ((toSend = getOutboundPackets().poll()) != null)
                    out.writeObject(toSend);

                // block until a response is received
                Object obj = in.readObject();
                // verify that it's a packet and process it
                if (obj instanceof Packet received)
                    getDispatcher().dispatch(received, this);
            }
        } catch (EOFException e) {
            logger.log(Level.WARNING, "Connection closed unexpectedly", e);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error with session communication", e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.WARNING, "Unrecognized packet", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error closing session socket", e);
            }
        }
    }

}
