package edu.uprb.quizzilla.game;

import edu.uprb.quizzilla.network.Session;
import edu.uprb.quizzilla.network.packets.PacketChatMessage;

import java.io.Serializable;

public class Player implements Serializable {

    private final String username;
    private final transient Session session;
    private int score;

    public Player(String username, Session session) {
        this.username = username;
        this.session = session;
    }

    public void sendMessage(String message, Object... args) {
        session.sendPacket(new PacketChatMessage(
                username, String.format(message, args)));
    }

    public String getUsername() {
        return username;
    }

    public Session getSession() {
        return session;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
