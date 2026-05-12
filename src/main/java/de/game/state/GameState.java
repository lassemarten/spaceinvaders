package main.java.de.game.state;

import main.java.de.game.entity.Bullet;
import main.java.de.game.entity.InvaderSwarm;
import main.java.de.game.entity.Player;

import java.util.List;

/**
 * Reines Daten-Objekt (Snapshot).
 * Renderer liest nur hieraus – keine Logik.
 */
public record GameState(
    Player       player,
    InvaderSwarm swarm,
    List<Bullet> bullets,
    int level,
    int          score,
    Phase        phase
) {
    public enum Phase { PLAYING, GAME_OVER, WON, PAUSED }
}
