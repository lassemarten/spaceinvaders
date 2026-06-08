package main.java.de.game.effect;

import main.java.de.game.entity.Player;

/**
 * Shield-Effekt: macht den Spieler unverwundbar.
 */
public class ShieldEffect implements ItemEffect {

    @Override
    public void apply(Player player) {
        player.setInvincible(true);
    }

    @Override
    public void remove(Player player) {
        player.setInvincible(false);
    }

    @Override
    public String getName() { return "SHIELD"; }
}
