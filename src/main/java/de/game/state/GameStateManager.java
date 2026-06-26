package main.java.de.game.state;

import main.java.de.game.Datenbank.Highscore;
import main.java.de.game.Datenbank.HighscoreEintrag;
import main.java.de.game.entity.Bullet;
import main.java.de.game.entity.Invader;
import main.java.de.game.entity.InvaderSwarm;
import main.java.de.game.entity.Player;
import main.java.de.game.input.InputHandler;
import main.java.de.game.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Enthält die gesamte Spiellogik.
 * Liest Input, aktualisiert Entities, prüft Kollisionen und Gewinn-/Verlustbedingungen.
 * Produziert nach jedem Update einen neuen GameState-Snapshot für den Renderer.
 * Verwaltet die Highscore-Erfassung und -Speicherung
 */

public class GameStateManager {

    private final InputHandler input;

    private Player       player;
    private InvaderSwarm swarm;
    private List<Bullet> bullets;
    private int          level;
    private int          score;
    private GameState.Phase phase;
    private int          dash_cooldown;

    //Für die Trefferquote
    private int shotsFired = 0;
    private int shotsHit = 0;

    //Für Highscore
    private List<HighscoreEintrag> highscores = new ArrayList<>();

    private String playerName = "";

    public void startGame(String name) {
        this.playerName = name;
        System.out.println(playerName);
        reset(); // setzt phase auf PLAYING
    }

    public String getPlayerName() { return playerName; }

    public GameStateManager(InputHandler input) {
        this.input = input;
        reset();
        phase = GameState.Phase.START;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public void update(long deltaMs) {
        if (phase == GameState.Phase.START) {
            return;
        }

        if (phase == GameState.Phase.GAME_OVER) {
            if (input.consumeRestart()) reset();
            return;
        }

        if (phase == GameState.Phase.PAUSED) {
            if (input.consumeRestart()) phase = GameState.Phase.PLAYING;
            return;
        }

        handleInput();
        updateBullets();
        handleInvaderUpdate();
        checkCollisions();
        checkWinLoss();
        updateCooldown();
    }

    public GameState getCurrentState() {
        return new GameState(player, swarm, List.copyOf(bullets), level, score, phase, dash_cooldown);
    }

    // -------------------------------------------------------------------------
    // Private logic
    // -------------------------------------------------------------------------

    private void handleInput() {
        if (input.consumeEscape()){
            phase = GameState.Phase.PAUSED;
            return;
        }

        if (input.isLeft()) {
            if (input.dash() && dash_cooldown >= Constants.DASH_COOLDOWN){
                player.dashLeft();
                dash_cooldown = 0;
            }else {
                player.moveLeft();
            }
        }
        if (input.isRight()){
            if (input.dash() && dash_cooldown >= Constants.DASH_COOLDOWN){
                player.dashRight();
                dash_cooldown = 0;
            }else {
                player.moveRight();
            }
        }

        boolean noPlayerBullet = bullets.stream()
            .noneMatch(b -> b.getOwner() == Bullet.Owner.PLAYER && b.isActive());

        if (input.consumeShoot() && noPlayerBullet) {
            bullets.add(player.shoot());
            shotsFired++; //Schuss wird gezählt
        }
        input.keyReset();
    }

    private void updateBullets() {
        bullets.forEach(Bullet::update);
        bullets.removeIf(b -> !b.isActive());
    }

    private void updateCooldown() {
        dash_cooldown += 1;
    }

    private void handleInvaderUpdate() {
        Bullet shot = swarm.update();
        if (shot != null) bullets.add(shot);
    }

    private void checkCollisions() {
        Iterator<Bullet> bi = bullets.iterator();
        while (bi.hasNext()) {
            Bullet b = bi.next();

            if (b.getOwner() == Bullet.Owner.PLAYER) {
                for(Bullet bul : bullets) {
                    if(b.collidesWith(bul) && bul.getOwner() == Bullet.Owner.INVADER){
                        bul.setActive(false);
                        b.setActive(false);
                        score += main.java.de.game.util.Constants.SCORE_PER_BULLET_ON_BULLET;
                        shotsHit++; //Treffer wird in Quote mitgezählt.
                        break;
                    }
                }

                // Spieler-Kugel trifft Invasor
                for (Invader inv : swarm.getActive()) {
                    if (b.collidesWith(inv)) {
                        switch (inv.getColor()){
                            case "green":
                                score += main.java.de.game.util.Constants.SCORE_PER_KILL;
                                break;
                            case "red":
                                score += (int) Math.floor(main.java.de.game.util.Constants.SCORE_PER_KILL * 1.25);
                                break;
                            case "blue":
                                score += (int) Math.floor(main.java.de.game.util.Constants.SCORE_PER_KILL * 1.5);
                                break;
                            default:
                                score += main.java.de.game.util.Constants.SCORE_PER_KILL;
                                break;
                        }
                        inv.setActive(false);
                        b.setActive(false);
                        shotsHit++; //Treffer gezählt
                        break;
                    }
                }
            } else {
                // Invasoren-Kugel trifft Spieler
                if (b.collidesWith(player)) {
                    triggeredGameOver();
                }
            }
        }
        bullets.removeIf(b -> !b.isActive());
    }

    private void checkWinLoss() {
        if (swarm.isEmpty()) {
            level  += 1;
            player  = new Player();
            swarm   = new InvaderSwarm(level);
            bullets = new ArrayList<>();
            phase   = GameState.Phase.PLAYING;
        } else if (swarm.hasReachedBottom()) {
            triggeredGameOver();
        }
    }

    private void triggeredGameOver() {// Trigger GameOver-Methode
        if(phase != GameState.Phase.PLAYING) {//Nur um zu verhindern, dass es möglicherweise doppelt aufgerufen wird
            return;
        }
        phase = GameState.Phase.GAME_OVER;

        double quote = shotsFired > 0 ? (double) shotsHit / shotsFired : 0.0;

        try{
            Highscore.sendScore(playerName, score, quote);
            highscores = Highscore.loadScore();
        } catch (RuntimeException e) {
            System.err.println("Highscore konnte nicht gespeichert werden (DB nicht erreichbar): " + e.getCause().getMessage());
            // Spiel läuft trotzdem normal weiter
        }
        System.out.printf("Score gesendet: %s | %d Punkte | Quote: %.2f\n ", playerName, score, quote);
    }

    public List<HighscoreEintrag> getHighscores() {
        return highscores;
    }

    private void reset() {
        level   = 1;
        player  = new Player();
        dash_cooldown = Constants.DASH_COOLDOWN;
        swarm   = new InvaderSwarm(level);
        bullets = new ArrayList<>();
        score   = 0;
        phase   = GameState.Phase.PLAYING;
        shotsFired = 0;
        shotsHit = 0;
    }
}
