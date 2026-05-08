package de.game.entity;

import de.game.util.Constants;

/**
 * Ein einzelner Invasor.
 * Schießt selbstständig wenn shoot() aufgerufen wird.
 */
public class Invader extends Entity {

    public Invader(int x, int y) {
        super(x, y, Constants.INVADER_WIDTH, Constants.INVADER_HEIGHT);
    }

    public Bullet shoot() {
        int bulletX = x + width / 2 - Constants.BULLET_WIDTH / 2;
        int bulletY = y + height;
        return new Bullet(bulletX, bulletY, Bullet.Owner.INVADER);
    }
}
