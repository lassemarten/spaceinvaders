package main.java.de.game.renderer;

import main.java.de.game.entity.Bullet;
import main.java.de.game.entity.Invader;
import main.java.de.game.entity.Player;
import main.java.de.game.state.GameState;
import main.java.de.game.util.Constants;

import java.awt.*;

/**
 * Zustandsloser Renderer.
 * Bekommt einen GameState-Snapshot und zeichnet – keinerlei Spiellogik.
 */
public class GameRenderer {

    private static final Color COLOR_INVADER  = new Color(80, 220, 80);
    private static final Color COLOR_PLAYER   = Color.CYAN;
    private static final Color COLOR_BULLET_P = Color.YELLOW;
    private static final Color COLOR_BULLET_I = Color.RED;
    private static final Color COLOR_HUD      = Color.WHITE;
    private static final Color COLOR_HINT     = new Color(100, 100, 100);
    private static final Font  FONT_HUD       = new Font("Monospaced", Font.BOLD,  20);
    private static final Font  FONT_TITLE     = new Font("Monospaced", Font.BOLD,  60);
    private static final Font  FONT_SCORE     = new Font("Monospaced", Font.BOLD,  30);
    private static final Font  FONT_HINT      = new Font("Monospaced", Font.PLAIN, 18);

    public void render(Graphics2D g2, GameState state) {
        enableAntialiasing(g2);

        switch (state.phase()) {
            case PLAYING   -> renderGame(g2, state);
            case GAME_OVER -> renderOverlay(g2, "VERLOREN!", "ENTER zum Neustart", Color.RED, state.level(),   state.score());
            case WON       -> renderOverlay(g2, "GEWONNEN!", "ENTER zum Neustart", Color.GREEN, state.level(),  state.score());  //Gibts Momentan nicht
            case PAUSED    -> renderOverlay(g2, "PAUSIERT!","ENTER zum Fortsetzen", Color.WHITE, state.level(),   state.score());
            case START     -> renderOverlay(g2, "HALLO SPIELER*IN", "ENTER zum Spielen", Color.PINK, 1,   0);
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
        g2.setColor(Color.WHITE);
        fill_height =(int)(h * ((double) state.dash_cooldown()/Constants.DASH_COOLDOWN));
        if (fill_height >= h) {
            fill_height = h;
            g2.fillRect(x, y, w, fill_height);
        }else {
            g2.fillRect(x, y, w, fill_height);
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(x, y + fill_height, w, h - fill_height);
        }
    }

    private void renderInvaders(Graphics2D g2, GameState state) {
        for (Invader inv : state.swarm().getActive()) {
            int x = inv.getX(), y = inv.getY(), w = inv.getWidth(), h = inv.getHeight();

            g2.setColor(COLOR_INVADER);
            g2.fillRoundRect(x, y, w, h, 8, 8);

            // Augen
            g2.setColor(Color.BLACK);
            g2.fillOval(x + 7,  y + 8, 9, 9);
            g2.fillOval(x + 24, y + 8, 9, 9);

            // Mund (Zähne)
            g2.setColor(new Color(30, 150, 30));
            g2.fillRect(x + 8, y + 20, 5, 5);
            g2.fillRect(x + 17, y + 20, 5, 5);
            g2.fillRect(x + 26, y + 20, 5, 5);
        }
    }

    private void renderPlayer(Graphics2D g2, Player player) {
        int x = player.getX(), y = player.getY();
        int w = player.getWidth();

        // Rumpf
        g2.setColor(COLOR_PLAYER);
        int[] px = { x + w / 2, x,     x + w };
        int[] py = { y - 20,    y + 10, y + 10 };
        g2.fillPolygon(px, py, 3);

        // Cockpit
        g2.setColor(new Color(0, 180, 255));
        g2.fillRect(x + 5, y, w - 10, 12);
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
        String hint = "← → Bewegen   LEERTASTE Schießen   ESC Pausieren";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(hint, (Constants.SCREEN_WIDTH - fm.stringWidth(hint)) / 2, Constants.SCREEN_HEIGHT - 10);
    }

    // -------------------------------------------------------------------------
    // Overlay (Game Over / Won / PAUSED / START)
    // -------------------------------------------------------------------------

    private void renderOverlay(Graphics2D g2, String title, String untertitle, Color titleColor,int level, int score) {
        int cx = Constants.SCREEN_WIDTH / 2;
        int cy = Constants.SCREEN_HEIGHT / 2;

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
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void drawCentered(Graphics2D g2, String text, int cx, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, cx - fm.stringWidth(text) / 2, y);
    }

    private void enableAntialiasing(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,    RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
