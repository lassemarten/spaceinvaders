package main.java.de.game.entity.items;

import main.java.de.game.effect.ItemEffect;
import main.java.de.game.entity.Entity;
import main.java.de.game.util.Constants;

/**
 * Basis aller Items.
 * Fällt vom oberen Bildschirmrand nach unten.
 * Trägt einen ItemEffect, der beim Aufsammeln aktiviert wird.
 */
public abstract class Item extends Entity {

    public static final int ITEM_WIDTH  = 24;
    public static final int ITEM_HEIGHT = 24;
    public static final int FALL_SPEED  = 2;

    private final ItemEffect effect;
    private final int        durationTicks;

    protected Item(int x, ItemEffect effect, int durationTicks) {
        super(x, 0, ITEM_WIDTH, ITEM_HEIGHT);
        this.effect        = effect;
        this.durationTicks = durationTicks;
    }

    public void update() {
        y += FALL_SPEED;
        if (y > Constants.SCREEN_HEIGHT) active = false;
    }

    public ItemEffect getEffect()        { return effect; }
    public int        getDurationTicks() { return durationTicks; }
}