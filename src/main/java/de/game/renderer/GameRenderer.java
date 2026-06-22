package main.java.de.game.renderer;

import main.java.de.game.entity.Bullet;
import main.java.de.game.entity.Invader;
import main.java.de.game.entity.Player;
import main.java.de.game.state.GameState;
import main.java.de.game.state.GameStateManager;
import main.java.de.game.util.Constants;
import main.java.de.game.input.InputHandler;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import main.java.de.game.Datenbank.HighscoreEintrag;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameRenderer {

    private static final Color COLOR_INVADERGreen  = new Color(80, 220, 80);
    private static final Color COLOR_INVADERRed  = new Color(220, 80, 80);
    private static final Color COLOR_INVADERBlue  = new Color(80, 80, 220);
    private static final Color COLOR_PLAYER   = Color.CYAN;
    private static final Color COLOR_BULLET_P = Color.YELLOW;
    private static final Color COLOR_BULLET_I = Color.RED;
    private static final Color COLOR_HUD      = Color.WHITE;
    private static final Color COLOR_HINT     = Color.LIGHT_GRAY;
    private static final Font  FONT_HUD       = new Font("Monospaced", Font.BOLD,  20);
    private static final Font  FONT_TITLE     = new Font("Monospaced", Font.BOLD,  60);
    private static final Font  FONT_SCORE     = new Font("Monospaced", Font.BOLD,  30);
    private static final Font  FONT_HINT      = new Font("Monospaced", Font.BOLD, 18);

    private final GameStateManager stateManager;
    private final JPanel panel;
    private final JTextField nameInput;
    private final InputHandler input;
    private BufferedImage background;


    public GameRenderer(JPanel panel, InputHandler input, GameStateManager stateManager) {
        this.panel        = panel;
        this.input        = input;
        this.stateManager = stateManager;

        nameInput = new JTextField(15);
        nameInput.setFont(FONT_HINT);
        nameInput.setHorizontalAlignment(JTextField.CENTER);
        nameInput.setVisible(false);

        nameInput.addActionListener(e -> {
            String name = nameInput.getText().trim();
            if (!name.isEmpty()) {
                input.setNameInputActive(false);
                stateManager.startGame(name); // <-- Name auslesen + Spiel starten
            }
        });

        panel.setLayout(null);
        panel.add(nameInput);

        try {
            background = ImageIO.read(
                    Objects.requireNonNull(getClass().getResourceAsStream(Constants.TECTUR_BACKGROUND))
            );
        } catch (IOException | NullPointerException e) {
            background = null;
        }
    }

    public void render(Graphics2D g2, GameState state) {
        enableAntialiasing(g2);

        if (background != null) {
            g2.drawImage(background, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        }

        if (nameInput.isVisible()) {
            nameInput.setVisible(false);
            SwingUtilities.invokeLater(() -> panel.requestFocusInWindow()); // Fokus zurück
        }

        switch (state.phase()) {
            case PLAYING   -> renderGame(g2, state);
            case GAME_OVER -> renderOverlay(g2, "VERLOREN!",  "ENTER zum Neustart",   Color.RED,   state.level(), state.score());
            case WON       -> renderOverlay(g2, "GEWONNEN!",  "ENTER zum Neustart",   Color.GREEN, state.level(), state.score());
            case PAUSED    -> renderOverlay(g2, "PAUSIERT!",  "ENTER zum Fortsetzen", Color.WHITE, state.level(), state.score());
            case START     -> renderStartscreen(g2, "HALLO SPIELER*IN", "ENTER zum Spielen", Color.PINK);
        }
    }

    // -------------------------------------------------------------------------
    // Playing
    // -------------------------------------------------------------------------

    private void renderGame(Graphics2D g2, GameState state) {
        renderInvaders(g2, state);
        renderPlayer(g2, state.player());
        renderBullets(g2, state);
        renderHUD(g2, state);
        renderDash(g2, state);
    }

    private void renderDash(Graphics2D g2, GameState state) {
        int x = Constants.DASH_X, y = Constants.DASH_Y, w = 25, h = 75;
        int fill_height;
        g2.setColor(Color.YELLOW);
        fill_height = (int)(h * ((double) state.dash_cooldown() / Constants.DASH_COOLDOWN));
        if (fill_height >= h) {
            fill_height = h;
            g2.fillRect(x, y, w, fill_height);
        } else {
            g2.fillRect(x, y, w, fill_height);
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(x, y + fill_height, w, h - fill_height);
        }
    }

    private void renderInvaders(Graphics2D g2, GameState state) {
        for (Invader inv : state.swarm().getActive()) {
            int x = inv.getX(), y = inv.getY(), w = inv.getWidth(), h = inv.getHeight();

            BufferedImage sprite = inv.getSprite();
            if (sprite != null) {
                g2.drawImage(sprite, x, y, w, h, null);
                continue;
            }

        }
    }

    private void renderPlayer(Graphics2D g2, Player player) {
        int x = player.getX(), y = player.getY();
        int w = player.getWidth(), h = player.getHeight();

        BufferedImage sprite = player.getSprite();
        if (sprite != null) {
            g2.drawImage(sprite, x, y, w, h, null);
        }
    }

    private void renderBullets(Graphics2D g2, GameState state) {
        for (Bullet b : state.bullets()) {
            g2.setColor(b.getOwner() == Bullet.Owner.PLAYER ? COLOR_BULLET_P : COLOR_BULLET_I);
            g2.fillRoundRect(b.getX(), b.getY(), b.getWidth(), b.getHeight(), b.getWidth(), b.getHeight());
        }
    }

    private void renderHUD(Graphics2D g2, GameState state) {
        g2.setColor(COLOR_HUD);
        g2.setFont(FONT_HUD);
        g2.drawString("Level:" + state.level(), 20, 30);
        g2.drawString("Punkte: " + state.score(), 20, 55);
        g2.drawString("Invasoren: " + state.swarm().getActive().size(), 20, 80);

        g2.setColor(COLOR_HINT);
        g2.setFont(FONT_HINT);
        String hint = "SHIFT Dash   ← → Bewegen   LEERTASTE Schießen   ESC Pausieren";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(hint, (Constants.SCREEN_WIDTH - fm.stringWidth(hint)) / 2, Constants.SCREEN_HEIGHT - 10);
    }

    // -------------------------------------------------------------------------
    // Overlay
    // -------------------------------------------------------------------------

    private void renderStartscreen(Graphics2D g2, String title, String untertitle, Color titleColor) {
        int cx = Constants.SCREEN_WIDTH / 2;
        int cy = Constants.SCREEN_HEIGHT / 2;

        g2.setColor(titleColor);
        g2.setFont(FONT_TITLE);
        drawCentered(g2, title, cx, cy - 40);

        g2.setColor(COLOR_HINT);
        g2.setFont(FONT_HINT);
        drawCentered(g2, untertitle, cx, cy + 110);

        drawCenteredInput(cx, cy + 150);
    }

    private void renderOverlay(Graphics2D g2, String title, String untertitle, Color titleColor, int level, int score) {
        int cx = Constants.SCREEN_WIDTH / 2;
        int cy = Constants.SCREEN_HEIGHT / 5;

        g2.setColor(titleColor);
        g2.setFont(FONT_TITLE);
        drawCentered(g2, title, cx, cy - 40);

        g2.setColor(COLOR_HUD);
        g2.setFont(FONT_SCORE);
        drawCentered(g2, "Level: " + level, cx, cy + 20);

        g2.setColor(COLOR_HUD);
        g2.setFont(FONT_SCORE);
        drawCentered(g2, "Punkte: " + score, cx, cy + 65);

        g2.setColor(COLOR_HINT);
        g2.setFont(FONT_HINT);
        drawCentered(g2, untertitle, cx, cy + 110);

        // Highscores anzeigen
        List<HighscoreEintrag> highscores = stateManager.getHighscores();

        int y = cy + 150;
        int xRank = cx - 200;
        int xName = cx - 120;
        int xScore = cx + 20;
        int xQuote = cx + 120;



        g2.setFont(FONT_HINT);

        if (highscores != null && !highscores.isEmpty()) {

            g2.setColor(Color.WHITE);
            drawCentered(g2, "Highscores:", cx, y);
            y += 25;

            g2.drawString("Rang", xRank, y);
            g2.drawString("Name", xName, y);
            g2.drawString("Punkte", xScore, y);
            g2.drawString("Quote", xQuote, y);

            y += 25;

            int rank = 1;

            for (HighscoreEintrag entry : highscores) {

                g2.drawString(rank + ".", xRank, y);
                g2.drawString(entry.getName(), xName, y);
                g2.drawString(String.valueOf(entry.getScore()), xScore, y);
                g2.drawString(String.format("%.2f", entry.getQuote()), xQuote, y);

                y += 25;
                rank++;

                if (rank > 10) break; // Top 10
            }
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void drawCentered(Graphics2D g2, String text, int cx, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, cx - fm.stringWidth(text) / 2, y);
    }

    private void drawCenteredInput(int cx, int y) {
        int w = 200, h = 30;
        nameInput.setBounds(cx - w / 2, y, w, h);
        if (!nameInput.isVisible()) {
            nameInput.setVisible(true);
            input.setNameInputActive(true); // <-- Enter blockieren
            SwingUtilities.invokeLater(() -> nameInput.requestFocusInWindow());
        }
    }

    // Namen auslesen – aufrufbar vom GameStateManager o.ä.
    public String getPlayerName() {
        return nameInput.getText().trim();
    }

    private void enableAntialiasing(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}