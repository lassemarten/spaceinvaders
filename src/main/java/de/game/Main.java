package main.java.de.game;

import main.java.de.game.core.Game;
import main.java.de.game.Datenbank.Server;

import javax.swing.*;

/**
 * Einstiegspunkt der Anwendung.ad
 * Startet das Spiel auf dem Swing Event Dispatch Thread.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Server.starteServer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.start();
        });
    }
}
