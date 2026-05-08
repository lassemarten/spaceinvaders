package de.game;

import de.game.core.Game;

import javax.swing.*;

/**
 * Einstiegspunkt der Anwendung.
 * Startet das Spiel auf dem Swing Event Dispatch Thread.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            game.start();
        });
    }
}
