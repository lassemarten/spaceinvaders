package main.java.de.game.core;

/**
 * Jedes Objekt das am Game Loop teilnimmt implementiert dieses Interface.
 */
public interface Updatable {
    /** @param deltaMs vergangene Zeit seit letztem Frame in Millisekunden */
    void update(long deltaMs);
    void render();
}
