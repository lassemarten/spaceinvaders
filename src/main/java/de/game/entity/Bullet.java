package main.java.de.game.entity;

import main.java.de.game.util.Constants;

/**
 * Kugel – von Spieler oder Invasor abgefeuert.
 * Owner-Enum verhindert Friendly-Fire-Checks.
 */
public class Bullet extends Entity {

    public enum Owner { PLAYER, INVADER }

    private final Owner owner;

    public Bullet(int x, int y, Owner owner) {
        super(x, y, Constants.BULLET_WIDTH, Constants.BULLET_HEIGHT);
        this.owner = owner;
    }

    public void update() {
        if (owner == Owner.PLAYER) {
            y -= Constants.BULLET_SPEED;
            if (y + height < 0) active = false;
        } else {
            y += Constants.INVADER_BULLET_SPEED;
            if (y > Constants.SCREEN_HEIGHT) active = false;
        }
    }

    public Owner getOwner() { return owner; }
}
