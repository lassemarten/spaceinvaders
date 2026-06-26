package main.java.de.game.entity;

import main.java.de.game.util.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Die Klasse "Player" stellt die Spielfigur dar.
 *Sie verwaltet Position, Bewegung (inkl. Dash), Schießen und das Laden des Sprites.
 * Außerdem kann die Spielerfigur für einen Neustart zurückgesetzt werden.
 */

public class Player extends Entity {

    private BufferedImage sprite;

    public Player() {
        super(
                Constants.SCREEN_WIDTH / 2 - Constants.PLAYER_WIDTH / 2,
                Constants.PLAYER_Y,
                Constants.PLAYER_WIDTH,
                Constants.PLAYER_HEIGHT
        );
        try {
            sprite = ImageIO.read(
                    Objects.requireNonNull(getClass().getResourceAsStream(Constants.TECTUR_PLAYER))
            );
        } catch (IOException | NullPointerException e) {
            sprite = null; // Fallback auf gezeichnetes Schiff
        }
    }

    public BufferedImage getSprite() { return sprite; }

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
