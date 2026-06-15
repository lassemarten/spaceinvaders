package main.java.de.game.entity;

import main.java.de.game.util.Constants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Ein einzelner Invasor.
 * Schießt selbstständig wenn shoot() aufgerufen wird.
 */
public class Invader extends Entity {

    protected final String color;

    private static final Map<String, BufferedImage> SPRITE_CACHE = new HashMap<>();

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

    public BufferedImage getSprite() {
        return SPRITE_CACHE.computeIfAbsent(color, Invader::loadSprite);
    }

    private static BufferedImage loadSprite(String color) {
        String filename = switch (color) {
            case "red"  -> "/sprites/invaderRed.png";
            case "blue" -> "/sprites/invaderBlue.png";
            default     -> "/sprites/invaderGreen.png";
        };
        try {
            return ImageIO.read(
                    Objects.requireNonNull(Invader.class.getResourceAsStream(filename))
            );
        } catch (IOException | NullPointerException e) {
            return null; // Fallback auf gezeichneten Invader
        }
    }
}