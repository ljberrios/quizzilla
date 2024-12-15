package edu.uprb.quizzilla.game;

import edu.uprb.quizzilla.ServerSession;
import edu.uprb.quizzilla.util.Countdown;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static edu.uprb.quizzilla.util.Colors.*;

public class Game {

    private final Map<UUID, Player> players = new ConcurrentHashMap<>();
    private final List<Trivia> trivia = Collections.synchronizedList(new ArrayList<>());
    private final List<UUID> correctAnswers = Collections.synchronizedList(new ArrayList<>());
    private final UUID host;
    private final GameConfig config;
    private GameState state = GameState.LOBBY;
    private String category;
    private Trivia currentTrivia;
    private Thread timerThread;

    public Game(UUID host, GameConfig config) {
        this.host = host;
        this.config = config;
    }

    public void start() {
        state = GameState.IN_PROGRESS;

        if (category == null) {
            setCategory(config.getRandomCategory());
            broadcast(GREEN + BOLD + "A random category " +
                    "has been chosen: " + CYAN + DIM + category);
        }

        startNewRound();
        broadcastSound(GameSounds.START);
    }

    public void end() {
        state = GameState.ENDING;
        category = null;
        trivia.clear();
        broadcast(RED + BOLD + "Game Over.");
        showScores();
        Countdown.start(10, 1,
                (remaining) -> {
                    if (remaining > 5)
                        playFireworks();
                }, () -> {
                    broadcast(RED + BOLD + "Restarting...");
                    reset();
                });
    }

    private void playFireworks() {
        broadcastSound(GameSounds.FW_LAUNCH);
        broadcastSound(GameSounds.FW_TWINKLE);
        broadcastSound(GameSounds.FW_BLAST);
    }

    private void reset() {
        state = GameState.LOBBY;

        players.values().forEach(player -> {
            player.setScore(0);
            player.setReady(false);
        });

        broadcast(GREEN + "Type " + CYAN + "/ready"
                + GREEN + " when you're ready to play.");
    }

    public synchronized void ready(UUID uuid) {
        if (state != GameState.LOBBY) {
            print(RED + "Game is in progress");
            return;
        }

        var player = players.get(uuid);
        if (player.isReady()) {
            player.setReady(false);
            return;
        }

        player.setReady(true);
        broadcast(GREEN + player.getUsername() + " is ready to play");
        for (Player other : players.values())
            if (other != player && !other.isReady())
                return;

        // all ready, start countdown
        Countdown.start(5, 1,
                remaining -> {
                    broadcastf(GREEN + "Game starting in "
                            + YELLOW + "%.2f seconds", remaining);
                    broadcastSound(GameSounds.HAT);
                },
                this::start);
    }

    public synchronized void answer(UUID uuid, String answer) {
        if (state != GameState.IN_PROGRESS
                || correctAnswers.contains(uuid)
                || currentTrivia == null)
            return;

        Player player = players.get(uuid);
        if (player.isCooldownActive()) {
            player.sendMessage(RED + "You must wait to answer again");
            return;
        }

        player.startAnswerCooldown(config.getTimeLimit());

        if (!answer.equalsIgnoreCase(currentTrivia.getAnswer())) {
            player.sendMessage(RED + "Wrong answer!");
            return;
        }

        correctAnswers.add(uuid);
        int reward = 10 - correctAnswers.indexOf(uuid) * 2;
        if (reward <= 0)
            reward = 1;

        player.setScore(player.getScore() + reward);
        broadcastf(CYAN + "%s" + GREEN + " answered correctly! "
                + YELLOW + BOLD + "+%d", player.getUsername(), reward);

        if (correctAnswers.size() == players.size()) {
            timerThread.interrupt();
            broadcast(GREEN + "Everyone has answered correctly!");
            startNewRound();
        }
    }

    private void startNewRound() {
        if (currentTrivia != null)
            broadcast(GREEN + "The correct answer was "
                    + UNDERLINE + CYAN + currentTrivia.getAnswer());

        currentTrivia = null;
        correctAnswers.clear();

        if (trivia.isEmpty()) {
            end();
            return;
        }

        if (trivia.size() != config.getTrivia(category).size()) {
            showScores();
            Countdown.start(5, 1, this::chooseQuestion);
            return;
        }

        chooseQuestion();
    }

    private void chooseQuestion() {
        broadcastf(GREEN + "Choosing new question...");
        Countdown.start(5, 1, () -> {
            currentTrivia = trivia.removeLast();
            broadcast(YELLOW + BOLD + "QUESTION: "
                    + GREEN + DIM + currentTrivia.getQuestion());

            var choices = currentTrivia.getChoices();
            choices.forEach((letter, choice)
                    -> broadcastf(BLUE + "%c. %s", letter, choice));

            broadcastSound(GameSounds.POP);
            startRoundTimer();

            players.values().forEach(Player::liftAnswerCooldown);
        });
    }

    private void startRoundTimer() {
        int limit = config.getTimeLimit();
        timerThread = Countdown.start(limit, 10,
                (remaining) -> broadcast(GREEN +
                        remaining + " seconds remaining to answer!"),
                () -> {
                    broadcast(GREEN + "Time's up!");
                    startNewRound();
                });
    }

    public void join(ServerSession session, String username) {
        Player player = new Player(username, session);
        if (state != GameState.LOBBY) {
            player.sendMessage(RED + "Game is in progress");
            return;
        }

        UUID id = session.getID();
        players.put(id, player);
        broadcast(CYAN + username + GREEN + " joined the server");
        player.sendMessage(GREEN + "Type " + CYAN + "/ready"
                + GREEN + " when you're ready to play.");
    }

    public void leave(UUID id) {
        Player player = kick(id);
        broadcast(CYAN + player.getUsername() + GREEN + " left the server");
    }

    public Player kick(UUID id) {
        if (id.equals(host))
            return players.remove(id);

        Player player = players.remove(id);
        player.getSession().stop();
        return player;
    }

    public void showScores() {
        broadcast(GREEN + BOLD + "-- SCORES --");
        players.values().forEach(player ->
                broadcastf(YELLOW + "%s: " + CYAN + "%d",
                        player.getUsername(), player.getScore()));
    }

    public void broadcastf(String message, Object... args) {
        players.values().forEach(player -> player.sendMessage(message, args));
    }

    public void broadcast(String... messages) {
        players.values().forEach(player -> player.sendMessages(messages));
    }

    public void broadcastSound(GameSounds sound) {
        players.values().forEach(player -> player.playSound(sound));
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        trivia.addAll(config.getTrivia().get(category));
        Collections.shuffle(trivia);
    }

    public UUID getHost() {
        return host;
    }

    public Player getPlayer(UUID id) {
        return players.get(id);
    }
}
