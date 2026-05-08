package de.game.core;

import de.game.input.InputHandler;
import de.game.renderer.GameRenderer;
import de.game.state.GameStateManager;
import de.game.util.Constants;

import javax.swing.*;
import java.awt.*;

/**
 * Das Swing-Panel dient nur als Zeichenfläche.
 * Logik und Rendering sind ausgelagert.
 */
public class GamePanel extends JPanel implements Updatable {

    private final GameStateManager stateManager;
    private final GameRenderer     renderer;
    private final InputHandler     input;

    public GamePanel(GameStateManager stateManager, GameRenderer renderer, InputHandler input) {
        this.stateManager = stateManager;
        this.renderer     = renderer;
        this.input        = input;

        setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
    }

    @Override
    public void update(long deltaMs) {
        stateManager.update(deltaMs);
    }

    @Override
    public void render() {
        // Swing-sicher auf EDT
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderer.render((Graphics2D) g, stateManager.getCurrentState());
    }
}
