package fr.hyriode.hyriapi.tools.scoreboard;

public class Score {

    private int score;

    private final String playerName;

    public Score(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void removeScore(int score) {
        this.setScore(this.getScore() - score);
    }

    public void incrementScore() {
        this.setScore(this.score + 1);
    }

    public void addScore(int score) {
        this.setScore(this.score + score);
    }

}
