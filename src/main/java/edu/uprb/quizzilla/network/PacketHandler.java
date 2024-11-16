package edu.uprb.quizzilla.network;

/**
 * The implementations of this interface will handle/process
 * incoming packets from the server/client.
 *
 * @param <T> the {@link Packet} class to handle
 */
public interface PacketHandler<T extends Packet> {

    /**
     * Handle/process the given incoming {@link Packet}.
     *
     * @param packet the packet to handle/process
     * @param session the {@link Session} which received the packet
     */
    void handle(T packet, Session session);

}