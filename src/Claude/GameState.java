package de.game.state;

import de.game.entity.Bullet;
import de.game.entity.InvaderSwarm;
import de.game.entity.Player;

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
    public enum Phase { PLAYING, GAME_OVER, WON }
}
