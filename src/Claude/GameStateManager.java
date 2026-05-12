package de.game.state;

import de.game.entity.Bullet;
import de.game.entity.Invader;
import de.game.entity.InvaderSwarm;
import de.game.entity.Player;
import de.game.input.InputHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Enthält die gesamte Spiellogik.
 * Liest Input, aktualisiert Entities, prüft Kollisionen und Gewinn-/Verlustbedingungen.
 * Produziert nach jedem Update einen neuen GameState-Snapshot für den Renderer.
 */
public class GameStateManager {

    private final InputHandler input;

    private Player       player;
    private InvaderSwarm swarm;
    private List<Bullet> bullets;
    private int level;
    private int          score;
    private GameState.Phase phase;

    public GameStateManager(InputHandler input) {
        this.input = input;
        reset();
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public void update(long deltaMs) {
        if (phase != GameState.Phase.PLAYING) {
            if (input.consumeRestart()) reset();
            return;
        }

        handleInput();
        updateBullets();
        handleInvaderUpdate();
        checkCollisions();
        checkWinLoss();
    }

    public GameState getCurrentState() {
        return new GameState(player, swarm, List.copyOf(bullets), level, score, phase);
    }

    // -------------------------------------------------------------------------
    // Private logic
    // -------------------------------------------------------------------------

    private void handleInput() {
        if (input.isLeft())  player.moveLeft();
        if (input.isRight()) player.moveRight();

        boolean noPlayerBullet = bullets.stream()
            .noneMatch(b -> b.getOwner() == Bullet.Owner.PLAYER && b.isActive());

        if (input.consumeShoot() && noPlayerBullet) {
            bullets.add(player.shoot());
        }
    }

    private void updateBullets() {
        bullets.forEach(Bullet::update);
        bullets.removeIf(b -> !b.isActive());
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
                        score += de.game.util.Constants.SCORE_PER_BULLET_ON_BULLET;
                        break;
                    }
                }

                // Spieler-Kugel trifft Invasor
                for (Invader inv : swarm.getActive()) {
                    if (b.collidesWith(inv)) {
                        inv.setActive(false);
                        b.setActive(false);
                        score += de.game.util.Constants.SCORE_PER_KILL;
                        break;
                    }
                }
            } else {
                // Invasoren-Kugel trifft Spieler
                if (b.collidesWith(player)) {
                    phase = GameState.Phase.GAME_OVER;
                }
            }
        }
        bullets.removeIf(b -> !b.isActive());
    }

    private void checkWinLoss() {
        if (swarm.isEmpty()) {
            player  = new Player();
            swarm   = new InvaderSwarm(level);
            bullets = new ArrayList<>();
            level  += 1;
            phase   = GameState.Phase.PLAYING;
        } else if (swarm.hasReachedBottom()) {
            phase = GameState.Phase.GAME_OVER;
        }
    }

    private void reset() {
        level   =1;
        player  = new Player();
        swarm   = new InvaderSwarm(level);
        bullets = new ArrayList<>();
        score   = 0;
        phase   = GameState.Phase.PLAYING;
    }
}
