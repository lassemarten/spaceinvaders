package de.game.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Entkoppelt Tastatureingaben von der Spiellogik.
 * Zustand wird in Feldern gehalten – kein Callback-Chaos.
 */
public class InputHandler extends KeyAdapter {

    private boolean left;
    private boolean right;
    private boolean shootPressed;   // true für einen Frame
    private boolean restartPressed; // true für einen Frame
    private boolean escapePressed;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT,  KeyEvent.VK_A -> left  = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_SPACE -> shootPressed   = true;
            case KeyEvent.VK_ENTER -> restartPressed = true;
            case KeyEvent.VK_ESCAPE -> escapePressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT,  KeyEvent.VK_A -> left  = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> right = false;
        }
    }

    public boolean isLeft()  { return left; }
    public boolean isRight() { return right; }

    /** Konsumiert den Schuss-Input (nur einmal true pro Tastendruck) */
    public boolean consumeShoot() {
        boolean v = shootPressed;
        shootPressed = false;
        return v;
    }

    /** Konsumiert den Neustart-Input */
    public boolean consumeRestart() {
        boolean v = restartPressed;
        restartPressed = false;
        return v;
    }
    public boolean consumeEscape() {
        boolean v = escapePressed;
        escapePressed = false;
        return v;
    }

}
