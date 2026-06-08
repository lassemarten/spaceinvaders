package main.java.de.game.entity.items;

import main.java.de.game.effect.BlitzEffect;

/**
 * Blitz-Item: erhöht Speed und aktiviert Rapid-Fire für 10 Sekunden (600 Ticks @ 60 FPS).
 */
public class BlitzItem extends Item {

    public BlitzItem(int x) {
        super(x, new BlitzEffect(), 600);
    }
}
