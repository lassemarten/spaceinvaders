import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {

    // --- Konstanten ---
    static final int WIDTH  = 800;
    static final int HEIGHT = 600;
    static final int PLAYER_SPEED  = 5;
    static final int BULLET_SPEED  = 8;
    static final int INVADER_ROWS  = 3;
    static final int INVADER_COLS  = 10;
    static final int INVADER_W     = 40;
    static final int INVADER_H     = 30;
    static final int INVADER_GAP   = 15;

    // --- Spielzustand ---
    int playerX;
    boolean moveLeft, moveRight, shooting;
    int score = 0;
    boolean gameOver = false;
    boolean won = false;

    ArrayList<Rectangle> invaders = new ArrayList<>();
    ArrayList<Rectangle> bullets  = new ArrayList<>();
    ArrayList<Rectangle> invaderBullets = new ArrayList<>();

    int invaderDX = 2;           // Bewegungsrichtung der Invasoren
    int invaderTick = 0;         // Ticker für Invasoren-Schüsse
    Timer timer;

    // --- Konstruktor ---
    public SpaceInvaders() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        playerX = WIDTH / 2 - 20;
        spawnInvaders();

        timer = new Timer(16, this); // ~60 FPS
        timer.start();
    }

    void spawnInvaders() {
        invaders.clear();
        int startX = 80;
        int startY = 80;
        for (int row = 0; row < INVADER_ROWS; row++) {
            for (int col = 0; col < INVADER_COLS; col++) {
                int x = startX + col * (INVADER_W + INVADER_GAP);
                int y = startY + row * (INVADER_H + INVADER_GAP);
                invaders.add(new Rectangle(x, y, INVADER_W, INVADER_H));
            }
        }
    }

    // --- Game Loop ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver || won) return;

        // Spieler bewegen
        if (moveLeft  && playerX > 0)           playerX -= PLAYER_SPEED;
        if (moveRight && playerX < WIDTH - 40)   playerX += PLAYER_SPEED;

        // Spieler schießen (1 Kugel gleichzeitig erlaubt)
        if (shooting && bullets.isEmpty()) {
            bullets.add(new Rectangle(playerX + 17, HEIGHT - 70, 6, 15));
            shooting = false;
        }

        // Spieler-Kugeln bewegen
        Iterator<Rectangle> bi = bullets.iterator();
        while (bi.hasNext()) {
            Rectangle b = bi.next();
            b.y -= BULLET_SPEED;
            if (b.y < 0) { bi.remove(); continue; }

            // Treffer auf Invasor?
            Iterator<Rectangle> ii = invaders.iterator();
            boolean hit = false;
            while (ii.hasNext()) {
                Rectangle inv = ii.next();
                if (b.intersects(inv)) {
                    ii.remove();
                    bi.remove();
                    score += 10;
                    hit = true;
                    break;
                }
            }
        }

        // Invasoren bewegen
        boolean edgeHit = false;
        for (Rectangle inv : invaders) {
            inv.x += invaderDX;
            if (inv.x + inv.width >= WIDTH || inv.x <= 0) edgeHit = true;
        }
        if (edgeHit) {
            invaderDX = -invaderDX;
            for (Rectangle inv : invaders) inv.y += 20;
        }

        // Invasoren schießen (zufällig)
        invaderTick++;
        if (invaderTick >= 60 && !invaders.isEmpty()) {
            invaderTick = 0;
            Rectangle shooter = invaders.get((int)(Math.random() * invaders.size()));
            invaderBullets.add(new Rectangle(shooter.x + shooter.width / 2 - 3, shooter.y + shooter.height, 6, 15));
        }

        // Invasoren-Kugeln bewegen
        Rectangle player = new Rectangle(playerX, HEIGHT - 60, 40, 30);
        Iterator<Rectangle> ibi = invaderBullets.iterator();
        while (ibi.hasNext()) {
            Rectangle ib = ibi.next();
            ib.y += 4;
            if (ib.y > HEIGHT) { ibi.remove(); continue; }
            if (ib.intersects(player)) { gameOver = true; return; }
        }

        // Invasoren erreichen Boden?
        for (Rectangle inv : invaders) {
            if (inv.y + inv.height >= HEIGHT - 60) { gameOver = true; return; }
        }

        // Gewonnen?
        if (invaders.isEmpty()) won = true;

        repaint();
    }

    // --- Zeichnen ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (gameOver) {
            drawCenteredText(g2, "GAME OVER", 60, Color.RED, HEIGHT / 2 - 30);
            drawCenteredText(g2, "Punkte: " + score, 30, Color.WHITE, HEIGHT / 2 + 30);
            drawCenteredText(g2, "Leertaste zum Neustart", 20, Color.GRAY, HEIGHT / 2 + 70);
            return;
        }
        if (won) {
            drawCenteredText(g2, "GEWONNEN!", 60, Color.GREEN, HEIGHT / 2 - 30);
            drawCenteredText(g2, "Punkte: " + score, 30, Color.WHITE, HEIGHT / 2 + 30);
            drawCenteredText(g2, "Leertaste zum Neustart", 20, Color.GRAY, HEIGHT / 2 + 70);
            return;
        }

        // Invasoren
        for (Rectangle inv : invaders) {
            g2.setColor(new Color(80, 220, 80));
            g2.fillRect(inv.x, inv.y, inv.width, inv.height);
            // Augen
            g2.setColor(Color.BLACK);
            g2.fillOval(inv.x + 8,  inv.y + 8,  8, 8);
            g2.fillOval(inv.x + 24, inv.y + 8,  8, 8);
        }

        // Spieler (Dreieck-Raumschiff)
        g2.setColor(Color.CYAN);
        int[] xs = { playerX + 20, playerX, playerX + 40 };
        int[] ys = { HEIGHT - 80, HEIGHT - 50, HEIGHT - 50 };
        g2.fillPolygon(xs, ys, 3);
        g2.setColor(new Color(0, 180, 255));
        g2.fillRect(playerX + 5, HEIGHT - 55, 30, 15);

        // Spieler-Kugeln
        g2.setColor(Color.YELLOW);
        for (Rectangle b : bullets) g2.fillRect(b.x, b.y, b.width, b.height);

        // Invasoren-Kugeln
        g2.setColor(Color.RED);
        for (Rectangle ib : invaderBullets) g2.fillRect(ib.x, ib.y, ib.width, ib.height);

        // HUD
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        g2.drawString("Punkte: " + score, 20, 30);
        g2.drawString("Invasoren: " + invaders.size(), 20, 55);

        // Steuerung-Hinweis
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 14));
        g2.drawString("← → bewegen   Leertaste schießen", WIDTH / 2 - 150, HEIGHT - 10);
    }

    void drawCenteredText(Graphics2D g2, String text, int size, Color color, int y) {
        g2.setColor(color);
        g2.setFont(new Font("Monospaced", Font.BOLD, size));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, (WIDTH - fm.stringWidth(text)) / 2, y);
    }

    // --- Tastatureingaben ---
    @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT  -> moveLeft  = true;
            case KeyEvent.VK_RIGHT -> moveRight = true;
            case KeyEvent.VK_SPACE -> {
                if (gameOver || won) restart();
                else shooting = true;
            }
        }
    }
    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)  moveLeft  = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = false;
    }
    @Override public void keyTyped(KeyEvent e) {}

    void restart() {
        score = 0;
        gameOver = false;
        won = false;
        playerX = WIDTH / 2 - 20;
        bullets.clear();
        invaderBullets.clear();
        invaderDX = 2;
        spawnInvaders();
    }

    // --- Main ---
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new SpaceInvaders());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}  