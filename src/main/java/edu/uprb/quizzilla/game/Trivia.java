package edu.uprb.quizzilla.game;

import java.io.Serializable;
import java.util.*;

public class Trivia implements Serializable {

    private final String category;
    private final String question;
    private final String answer;
    private final Map<Character, String> choices = new LinkedHashMap<>();

    public Trivia(String category, String question, String answer) {
        this.category = category;
        this.question = question;
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Map<Character, String> getChoices() {
        return choices;
    }
}
