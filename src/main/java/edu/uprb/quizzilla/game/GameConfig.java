package edu.uprb.quizzilla.game;

import org.simpleyaml.configuration.ConfigurationSection;
import org.simpleyaml.configuration.file.YamlFile;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameConfig {

    private static final Logger logger = Logger.getLogger(GameConfig.class.getName());

    private final Map<String, List<Trivia>> trivia = new HashMap<>();
    private int maxSessions;
    private int timeLimit;
    private YamlFile yamlFile;

    public void init() {
        try {
            URL url = getClass().getClassLoader().getResource("config.yml");
            if (url == null)
                throw new IllegalStateException("Could not find config.yml");

            yamlFile = new YamlFile(url);
            if (!yamlFile.exists()) {
                yamlFile.createNewFile();
                logger.info("Created game config file");
            }

            // Load the entire file
            yamlFile.load();

            // Load game settings and trivia
            logger.info("Loading game settings and trivia...");
            loadTrivia();
            loadGameSettings();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error loading config file", e);
        }
    }

    private void loadGameSettings() {
        this.maxSessions = yamlFile.getInt("max-sessions");
        this.timeLimit = yamlFile.getInt("time-limit");
    }

    private void loadTrivia() {
        // Load the categories section
        var categoriesSection = yamlFile.getConfigurationSection("categories");
        if (categoriesSection == null)
            throw new IllegalStateException("No categories section found");

        // Iterate over categories and load their questions
        for (String category : categoriesSection.getKeys(false)) {
            var questions = loadCategoryQuestions(
                    categoriesSection.getConfigurationSection(category), category);
            this.trivia.put(category, questions);
        }
    }

    private List<Trivia> loadCategoryQuestions(
            ConfigurationSection categorySection, String category) {
        List<Trivia> trivias = new ArrayList<>();
        if (categorySection == null)
            return trivias;

        // Iterate over questions in the category
        for (String index : categorySection.getKeys(false)) {
            var questionSection = categorySection.getConfigurationSection(index);
            if (questionSection != null) {
                trivias.add(parseQuestion(questionSection, category));
            }
        }
        return trivias;
    }

    private Trivia parseQuestion(ConfigurationSection questionSection, String category) {
        String text = questionSection.getString("question");
        String answer = questionSection.getString("answer");

        var question = new Trivia(category, text, answer);

        // Load choices for the question, if any
        var choicesSection = questionSection.getConfigurationSection("choices");
        if (choicesSection != null) {
            choicesSection.getKeys(false).forEach(letter ->
                    question.getChoices().put(letter.charAt(0), choicesSection.getString(letter))
            );
        }

        return question;
    }

    public String getRandomCategory() {
        List<String> categories = new ArrayList<>(trivia.keySet());
        int randomIdx = ThreadLocalRandom.current().nextInt(categories.size());
        return categories.get(randomIdx);
    }

    public List<Trivia> getTrivia(String category) {
        return trivia.get(category);
    }

    public Map<String, List<Trivia>> getTrivia() {
        return trivia;
    }

    public int getMaxSessions() {
        return maxSessions;
    }

    public int getTimeLimit() {
        return timeLimit;
    }
}
