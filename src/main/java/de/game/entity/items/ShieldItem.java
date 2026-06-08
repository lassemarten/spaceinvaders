package main.java.de.game.entity.items;

import main.java.de.game.effect.ShieldEffect;

/**
 * Shield-Item: macht den Spieler für 8 Sekunden unverwundbar (480 Ticks @ 60 FPS).
 */
public class ShieldItem extends Item {

    public ShieldItem(int x) {
        super(x, new ShieldEffect(), 480);
    }
}
