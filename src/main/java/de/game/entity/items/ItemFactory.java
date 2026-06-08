package main.java.de.game.entity.items;

import main.java.de.game.util.Constants;

import java.util.Random;

/**
 * Erzeugt zufällige Items an einer zufälligen X-Position.
 * Neues Item-Typen hier registrieren – GameStateManager bleibt unberührt.
 */
public class ItemFactory {

    private static final Random RANDOM = new Random();

    private ItemFactory() {}

    public static Item createRandom() {
        int x = RANDOM.nextInt(Constants.SCREEN_WIDTH - Item.ITEM_WIDTH);
        return switch (RANDOM.nextInt(2)) {
            case 0  -> new BlitzItem(x);
            default -> new ShieldItem(x);
        };
    }
}
