package main.java.de.game.state;

import main.java.de.game.effect.ActiveEffect;
import main.java.de.game.entity.Bullet;
import main.java.de.game.entity.InvaderSwarm;
import main.java.de.game.entity.Player;
import main.java.de.game.entity.items.Item;

import java.util.List;

/**
 * Reines Daten-Objekt (Snapshot).
 * Renderer liest nur hieraus – keine Logik.
 */
public record GameState(
        Player             player,
        InvaderSwarm       swarm,
        List<Bullet>       bullets,
        List<Item>         items,
        List<ActiveEffect> activeEffects,
        int                level,
        int                score,
        Phase              phase,
        int                dash_cooldown
) {
    public enum Phase { PLAYING, GAME_OVER, WON, PAUSED, START }
}