package main.java.de.game.effect;

import main.java.de.game.entity.Player;

public class ActiveEffect {

    private final ItemEffect effect;
    private int remainingTicks;

    public ActiveEffect(ItemEffect effect, int durationTicks, Player player) {
        this.effect         = effect;
        this.remainingTicks = durationTicks;
        effect.apply(player);
    }

    public boolean tick(Player player) {
        remainingTicks--;
        if (remainingTicks <= 0) {
            effect.remove(player);
            return true;
        }
        return false;
    }

    public void forceRemove(Player player) {
        effect.remove(player);
        remainingTicks = 0;
    }

    public ItemEffect getEffect()         { return effect; }
    public int        getRemainingTicks() { return remainingTicks; }
}