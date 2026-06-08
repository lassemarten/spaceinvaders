package main.java.de.game.effect;

import main.java.de.game.entity.Player;

/**
 * Kapselt den Effekt eines aufgesammelten Items.
 * apply()  wird einmalig beim Aufsammeln aufgerufen.
 * remove() wird einmalig beim Ablaufen aufgerufen.
 * getName() liefert den Anzeigenamen für das HUD.
 */
public interface ItemEffect {
    void apply(Player player);
    void remove(Player player);
    String getName();
}
