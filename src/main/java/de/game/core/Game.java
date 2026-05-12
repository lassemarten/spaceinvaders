package main.java.de.game.core;

import main.java.de.game.input.InputHandler;
import main.java.de.game.renderer.GameRenderer;
import main.java.de.game.state.GameStateManager;
import main.java.de.game.util.Constants;

import javax.swing.*;

/**
 * Herzstück des Spiels.
 * Verwaltet das Fenster, den Game Loop und verdrahtet alle Subsysteme.
 */
public class Game {

    private final JFrame          frame;
    private final GamePanel       panel;
    private final GameLoop        loop;
    private final InputHandler    input;
    private final GameStateManager stateManager;
    private final GameRenderer    renderer;

    public Game() {
        input        = new InputHandler();
        stateManager = new GameStateManager(input);
        renderer     = new GameRenderer();
        panel        = new GamePanel(stateManager, renderer, input);
        loop         = new GameLoop(panel);
        frame        = buildFrame();
    }

    public void start() {
        frame.setVisible(true);
        loop.start();
    }

    private JFrame buildFrame() {
        JFrame f = new JFrame(Constants.TITLE);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.add(panel);
        f.pack();
        f.setLocationRelativeTo(null);
        panel.addKeyListener(input);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        return f;
    }
}
