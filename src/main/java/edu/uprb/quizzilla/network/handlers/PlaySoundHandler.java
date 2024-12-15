package edu.uprb.quizzilla.network.handlers;

import edu.uprb.quizzilla.network.PacketHandler;
import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketPlaySound;

public class PlaySoundHandler implements PacketHandler<PacketPlaySound> {
    @Override
    public void handle(PacketPlaySound packet, Session session) {
        packet.getSound().playSound();
    }
}
