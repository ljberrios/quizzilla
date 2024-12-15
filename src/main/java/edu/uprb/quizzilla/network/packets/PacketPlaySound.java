package edu.uprb.quizzilla.network.packets;

import edu.uprb.quizzilla.game.GameSounds;
import edu.uprb.quizzilla.network.Packet;

public class PacketPlaySound implements Packet {

    private final GameSounds sound;

    public PacketPlaySound(GameSounds sound) {
        this.sound = sound;
    }

    public GameSounds getSound() {
        return sound;
    }
}
