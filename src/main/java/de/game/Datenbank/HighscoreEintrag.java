package main.java.de.game.Datenbank;

public class HighscoreEintrag {
    public String name;
    public int score;
    public double quote;

    public HighscoreEintrag() {    } //Wird für GSON gebraucht (weil da leere Eintrage erstellt werden.

    public HighscoreEintrag(String name, int score, double quote) {
        this.name = name;
        this.score = score;
        this.quote = quote;
    }

    //Vielleicht besser, wenn es getter gibt ;-)
    public String getName(){
        return name;
    }
    public int getScore(){
        return score;
    }
    public double getQuote(){
        return quote;
    }
}