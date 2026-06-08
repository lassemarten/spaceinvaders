package main.java.de.game.state;

import main.java.de.game.effect.ActiveEffect;
import main.java.de.game.entity.Bullet;
import main.java.de.game.entity.Invader;
import main.java.de.game.entity.InvaderSwarm;
import main.java.de.game.entity.Player;
import main.java.de.game.entity.items.Item;
import main.java.de.game.entity.items.ItemFactory;
import main.java.de.game.input.InputHandler;
import main.java.de.game.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameStateManager {

    private final InputHandler input;
    private final Random       random = new Random();

    private Player             player;
    private InvaderSwarm       swarm;
    private List<Bullet>       bullets;
    private List<Item>         items;
    private List<ActiveEffect> activeEffects;
    private int                level;
    private int                score;
    private GameState.Phase    phase;
    private int                dashCooldown;
    private int                itemSpawnTick;
    private int                shootTick;

    private String playerName = "";

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public GameStateManager(InputHandler input) {
        this.input = input;
        reset();
        phase = GameState.Phase.START;
    }

    public void startGame(String name) {
        this.playerName = name;
        reset();
    }

    public String getPlayerName() { return playerName; }

    public void update(long deltaMs) {
        if (phase == GameState.Phase.START)     return;
        if (phase == GameState.Phase.GAME_OVER) { if (input.consumeRestart()) reset(); return; }

        handleInput();

        if (phase == GameState.Phase.PAUSED) { if (input.consumeRestart()) phase = GameState.Phase.PLAYING; return; }

        updateBullets();
        updateItems();
        updateEffects();
        handleInvaderUpdate();
        checkCollisions();
        checkItemPickup();
        checkWinLoss();
        updateCooldown();
        trySpawnItem();
    }

    public GameState getCurrentState() {
        return new GameState(
                player,
                swarm,
                List.copyOf(bullets),
                List.copyOf(items),
                List.copyOf(activeEffects),
                level,
                score,
                phase,
                dashCooldown
        );
    }

    // -------------------------------------------------------------------------
    // Private logic
    // -------------------------------------------------------------------------

    private void handleInput() {
        if (input.consumeEscape()) { phase = GameState.Phase.PAUSED; return; }

        if (input.isLeft()) {
            if (input.dash() && dashCooldown >= Constants.DASH_COOLDOWN) { player.dashLeft();  dashCooldown = 0; }
            else player.moveLeft();
        }
        if (input.isRight()) {
            if (input.dash() && dashCooldown >= Constants.DASH_COOLDOWN) { player.dashRight(); dashCooldown = 0; }
            else player.moveRight();
        }

        shootTick--;
        if (input.consumeShoot() && shootTick <= 0) {
            bullets.add(player.shoot());
            shootTick = Constants.SHOOT_COOLDOWN - player.getShootCooldownBonus();
        }
        input.keyReset();
    }

    private void updateBullets() {
        bullets.forEach(Bullet::update);
        bullets.removeIf(b -> !b.isActive());
    }

    private void updateItems() {
        items.forEach(Item::update);
        items.removeIf(i -> !i.isActive());
    }

    private void updateEffects() {
        activeEffects.removeIf(ae -> ae.tick(player));
    }

    private void updateCooldown() {
        dashCooldown++;
    }

    private void handleInvaderUpdate() {
        Bullet shot = swarm.update();
        if (shot != null) bullets.add(shot);
    }

    private void trySpawnItem() {
        itemSpawnTick--;
        if (itemSpawnTick <= 0) {
            items.add(ItemFactory.createRandom());
            itemSpawnTick = Constants.ITEM_SPAWN_INTERVAL_MIN
                    + random.nextInt(Constants.ITEM_SPAWN_INTERVAL_MAX - Constants.ITEM_SPAWN_INTERVAL_MIN);
        }
    }

    private void checkCollisions() {
        Iterator<Bullet> bi = bullets.iterator();
        while (bi.hasNext()) {
            Bullet b = bi.next();
            if (b.getOwner() == Bullet.Owner.PLAYER) {

                for (Bullet bul : bullets) {
                    if (b.collidesWith(bul) && bul.getOwner() == Bullet.Owner.INVADER) {
                        bul.setActive(false);
                        b.setActive(false);
                        score += Constants.SCORE_PER_BULLET_ON_BULLET;
                        break;
                    }
                }

                for (Invader inv : swarm.getActive()) {
                    if (b.collidesWith(inv)) {
                        score += switch (inv.getColor()) {
                            case "red"  -> (int) Math.floor(Constants.SCORE_PER_KILL * 1.25);
                            case "blue" -> (int) Math.floor(Constants.SCORE_PER_KILL * 1.5);
                            default     -> Constants.SCORE_PER_KILL;
                        };
                        inv.setActive(false);
                        b.setActive(false);
                        break;
                    }
                }

            } else {
                if (b.collidesWith(player) && !player.isInvincible()) {
                    phase = GameState.Phase.GAME_OVER;
                }
            }
        }
        bullets.removeIf(b -> !b.isActive());
    }

    private void checkItemPickup() {
        for (Item item : items) {
            if (item.isActive() && item.collidesWith(player)) {
                activeEffects.forEach(ae -> ae.forceRemove(player));
                activeEffects.clear();
                activeEffects.add(new ActiveEffect(item.getEffect(), item.getDurationTicks(), player));
                item.setActive(false);
                break;
            }
        }
        items.removeIf(i -> !i.isActive());
    }

    private void checkWinLoss() {
        if (swarm.isEmpty()) {
            level++;
            player        = new Player();
            swarm         = new InvaderSwarm(level);
            bullets       = new ArrayList<>();
            items         = new ArrayList<>();
            activeEffects = new ArrayList<>();
            phase         = GameState.Phase.PLAYING;
        } else if (swarm.hasReachedBottom()) {
            phase = GameState.Phase.GAME_OVER;
        }
    }

    private void reset() {
        level         = 1;
        score         = 0;
        dashCooldown  = Constants.DASH_COOLDOWN;
        shootTick     = 0;
        player        = new Player();
        swarm         = new InvaderSwarm(level);
        bullets       = new ArrayList<>();
        items         = new ArrayList<>();
        activeEffects = new ArrayList<>();
        itemSpawnTick = Constants.ITEM_SPAWN_INTERVAL_MIN;
        phase         = GameState.Phase.PLAYING;
    }
}