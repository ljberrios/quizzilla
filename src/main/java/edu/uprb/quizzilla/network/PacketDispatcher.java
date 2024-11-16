package edu.uprb.quizzilla.network;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all registered {@link PacketHandler}s and routes
 * all {@link Packet}s to their respective handler. There should only
 * be one instance of this class through which all handlers are registered.
 */
public class PacketDispatcher {

    private final Map<Class<? extends Packet>, PacketHandler<?>> handlers = new HashMap<>();

    /**
     * Register the given {@link PacketHandler} for the specified {@link Packet} class.
     *
     * @param packetType the packet class
     * @param handler the handler instance
     */
    public <T extends Packet> void registerHandler(Class<T> packetType, PacketHandler<T> handler) {
        handlers.put(packetType, handler);
    }

    /**
     * Routes {@link Packet}s to their respective {@link PacketHandler}.
     *
     * @param packet the packet
     * @param session the session which received the packet
     */
    @SuppressWarnings("unchecked")
    public <T extends Packet> void dispatch(T packet, Session session) {
        PacketHandler<T> handler = (PacketHandler<T>) handlers.get(packet.getClass());
        if (handler != null) {
            handler.handle(packet, session);
        } else {
            throw new IllegalStateException("No handler registered for packet: " + packet.getClass());
        }
    }
}
