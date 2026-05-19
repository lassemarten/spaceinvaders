package main.java.de.game.entity;

import main.java.de.game.util.Constants;

/**
 * Der Spieler.
 * Bewegt sich horizontal und schießt eine Kugel gleichzeitig.
 */
public class Player extends Entity {

    public Player() {
        super(
            Constants.SCREEN_WIDTH / 2 - Constants.PLAYER_WIDTH / 2,
            Constants.PLAYER_Y,
            Constants.PLAYER_WIDTH,
            Constants.PLAYER_HEIGHT
        );

    }

    public void moveLeft() {
        x = Math.max(0, x - Constants.PLAYER_SPEED);
    }

    public void moveRight() {
        x = Math.min(Constants.SCREEN_WIDTH - width, x + Constants.PLAYER_SPEED);
    }

    public void dashLeft(){
        x = Math.max(0, x - Constants.PLAYER_SPEED*70);
    }

    public void dashRight(){
        x = Math.min(Constants.SCREEN_WIDTH - width, x + Constants.PLAYER_SPEED*70);
    }

    public Bullet shoot() {
        int bulletX = x + width / 2 - Constants.BULLET_WIDTH / 2;
        int bulletY = y - Constants.BULLET_HEIGHT;
        return new Bullet(bulletX, bulletY, Bullet.Owner.PLAYER);
    }

    public void reset() {
        x = Constants.SCREEN_WIDTH / 2 - width / 2;
        active = true;
    }
}
