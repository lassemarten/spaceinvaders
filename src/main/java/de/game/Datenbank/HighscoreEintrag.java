package main.java.de.game.Datenbank;

public class HighscoreEintrag {
    public String name;
    public int score;
    public double quote;

    public HighscoreEintrag(String name, int score, double quote) {
        this.name = name;
        this.score = score;
        this.quote = quote;
    }
}