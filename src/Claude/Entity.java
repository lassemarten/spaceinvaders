package de.game.entity;

import java.awt.*;

/**
 * Basisklasse aller Spielobjekte.
 * Kapselt Position, Größe und Aktivitätsstatus.
 */
public abstract class Entity {

    protected int x, y;
    protected final int width, height;
    protected boolean active = true;

    protected Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width  = width;
        this.height = height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean collidesWith(Entity other) {
        return active && other.active && getBounds().intersects(other.getBounds());
    }

    public boolean isActive()          { return active; }
    public void    setActive(boolean v){ active = v; }
    public int     getX()              { return x; }
    public int     getY()              { return y; }
    public int     getWidth()          { return width; }
    public int     getHeight()         { return height; }
}
