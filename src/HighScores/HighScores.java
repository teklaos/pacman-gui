package HighScores;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HighScores implements Serializable {
    private final Map<String, Integer> highScores;

    public HighScores() {
        this.highScores = new HashMap<>();
    }

    public Map<String, Integer> getHighScores() {
        return highScores;
    }

    public void addHighScore(String username, Integer score) {
        this.highScores.put(username, score);
    }
}
