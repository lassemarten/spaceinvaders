package main.java.de.game.entity;

import main.java.de.game.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Verwaltet die gesamte Invasoren-Formation.
 * Verantwortlich für Gruppen-Bewegung und zufälliges Schießen.
 */
public class InvaderSwarm {

    private final List<Invader> invaders = new ArrayList<>();
    private final Random        random   = new Random();

    private int  dx        = Constants.INVADER_SPEED;
    private int  shootTick = 0;

    public InvaderSwarm(int level) {
        reset(level);
    }

    public void reset(int level) {
        invaders.clear();
        dx = Constants.INVADER_SPEED;
        shootTick = 0;
        int max_row = (int) Math.floor(Constants.INVADER_ROWS + Math.pow((1.25 * (level-1)),0.5));
        if (max_row > 7) max_row = 7;
        for (int row = 0; row < max_row; row++) {
            for (int col = 0; col < Constants.INVADER_COLS; col++) {
                int x = Constants.INVADER_START_X + col * (Constants.INVADER_WIDTH  + Constants.INVADER_H_GAP);
                int y = Constants.INVADER_START_Y + row * (Constants.INVADER_HEIGHT + Constants.INVADER_V_GAP);

                if(row < max_row - 4){
                    invaders.add(new Invader(x, y, "blue"));
                }else if(row < max_row - 2) {
                        invaders.add(new Invader(x, y, "red"));
                }else {
                    invaders.add(new Invader(x, y, "green"));
                }

            }
        }
    }

    /** Bewegt alle aktiven Invasoren. @return optionaler Schuss */
    public Bullet update() {
        moveFormation();
        return tryShoot();
    }

    private void moveFormation() {
        boolean hitEdge = invaders.stream()
            .filter(Entity::isActive)
            .anyMatch(inv -> inv.getX() + inv.getWidth() >= Constants.SCREEN_WIDTH || inv.getX() <= 0);

        if (hitEdge) {
            dx = -dx;
            invaders.stream().filter(Entity::isActive).forEach(inv -> inv.y += Constants.INVADER_STEP_DOWN);
        }

        invaders.stream().filter(Entity::isActive).forEach(inv -> inv.x += dx);
    }

    private Bullet tryShoot() {
        shootTick++;
        if (shootTick < Constants.INVADER_SHOOT_TICKS) return null;
        shootTick = 0;

        List<Invader> active = getActive();
        if (active.isEmpty()) return null;
        return active.get(random.nextInt(active.size())).shoot();
    }

    /** Prüft ob ein Invasor den Boden erreicht hat */
    public boolean hasReachedBottom() {
        return invaders.stream()
            .filter(Entity::isActive)
            .anyMatch(inv -> inv.getY() + inv.getHeight() >= Constants.PLAYER_Y);
    }

    public List<Invader> getActive() {
        return invaders.stream().filter(Entity::isActive).toList();
    }

    public boolean isEmpty() {
        return getActive().isEmpty();
    }
}
