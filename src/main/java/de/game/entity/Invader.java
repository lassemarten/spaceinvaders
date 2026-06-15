package main.java.de.game.entity;

import main.java.de.game.util.Constants;

/**
 * Ein einzelner Invasor.
 * Schießt selbstständig wenn shoot() aufgerufen wird.
 */
public class Invader extends Entity {

    protected final String color;

    public Invader(int x, int y, String color) {
        super(x, y, Constants.INVADER_WIDTH, Constants.INVADER_HEIGHT);
        this.color = color;
    }

    public Bullet shoot() {
        int bulletX = x + width / 2 - Constants.BULLET_WIDTH / 2;
        int bulletY = y + height;
        return new Bullet(bulletX, bulletY, Bullet.Owner.INVADER);
    }

    public String getColor() {
        return color;
    }
}
