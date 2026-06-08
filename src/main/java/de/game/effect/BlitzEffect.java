package main.java.de.game.effect;

import main.java.de.game.entity.Player;

public class BlitzEffect implements ItemEffect {

    private static final int SPEED_BONUS          = 6;
    private static final int SHOOT_COOLDOWN_BONUS = 12;

    @Override
    public void apply(Player player) {
        player.addSpeedBonus(SPEED_BONUS);
        player.addShootCooldownBonus(SHOOT_COOLDOWN_BONUS);
    }

    @Override
    public void remove(Player player) {
        player.addSpeedBonus(-SPEED_BONUS);
        player.addShootCooldownBonus(-SHOOT_COOLDOWN_BONUS);
    }

    @Override
    public String getName() { return "BLITZ"; }
}