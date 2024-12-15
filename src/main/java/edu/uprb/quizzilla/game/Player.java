package edu.uprb.quizzilla.game;

import edu.uprb.quizzilla.ServerSession;
import edu.uprb.quizzilla.network.packets.PacketChatMessage;
import edu.uprb.quizzilla.network.packets.PacketPlaySound;
import edu.uprb.quizzilla.util.Countdown;

import java.util.Arrays;

import static edu.uprb.quizzilla.util.Colors.*;

public class Player {

    private final String username;
    private final ServerSession session;
    private int score;
    private volatile boolean ready;
    private Thread cooldownThread;

    public Player(String username, ServerSession session) {
        this.username = username;
        this.session = session;
    }

    public void startAnswerCooldown(int timeLimit) {
        cooldownThread = Countdown.start(timeLimit / 3, 1,
                () -> {
                    liftAnswerCooldown();
                    sendMessage(MAGENTA + "Your answer cooldown has been lifted!");
                });
    }

    public void liftAnswerCooldown() {
        if (isCooldownActive())
            cooldownThread.interrupt();
    }

    public boolean isCooldownActive() {
        if (cooldownThread == null)
            return false;
        return cooldownThread.isAlive();
    }

    public void sendMessage(String message, Object... args) {
        session.sendPacket(new PacketChatMessage(
                null, String.format(message, args)));
    }

    public void sendMessages(String... messages) {
        Arrays.stream(messages).forEach(this::sendMessage);
    }

    public void playSound(GameSounds sound) {
        session.sendPacket(new PacketPlaySound(sound));
    }

    public String getUsername() {
        return username;
    }

    public ServerSession getSession() {
        return session;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
