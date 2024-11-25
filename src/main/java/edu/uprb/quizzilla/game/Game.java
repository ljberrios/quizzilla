package edu.uprb.quizzilla.game;

import static edu.uprb.quizzilla.util.Colors.*;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {

    private final TreeSet<Player> players = new TreeSet<>(getPlayerOrder());
    private final transient Map<String, List<Question>> questions;

    private String category;
    private Question currentQuestion;
    private final List<String> corrects = new ArrayList<>();
    private long roundTimer;

    public Game(GameConfig config) {
        this.questions = config.getQuestions();
    }

    public void answer(Player player, String answer) {
        if (!currentQuestion.getAnswer().equalsIgnoreCase(answer)) {
            broadcast(BOLD + "%s " + DIM + "answered " + UNDERLINE + "%s",
                    player.getUsername(), answer);
            broadcast(BOLD + "%s " + DIM + "has been awarded" + CYAN + "zero points",
                    player.getUsername());
            return;
        }

        corrects.add(player.getUsername());
        
    }

    public void join(Player player) {
        players.add(player);
    }

    public void leave(Player player) {
        players.remove(player);
        broadcast(BOLD + "%s " + DIM + "has left the game", player.getUsername());
    }

    public void broadcast(String message, Object... args) {
        players.forEach(player -> player.sendMessage(message, args));
    }

    public List<Question> getQuestions() {
        return questions.get(category);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
        // start new round
        this.roundTimer = System.currentTimeMillis();
    }

    private static Comparator<Player> getPlayerOrder() {
        return Comparator
                // First: Compare scores in descending order
                .comparingInt(Player::getScore).reversed()
                // Tie-breaker: Compare usernames in ascending order
                .thenComparing(Player::getUsername);
    }
}
