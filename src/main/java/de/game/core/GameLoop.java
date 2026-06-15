package main.java.de.game.core;

import main.java.de.game.util.Constants;

/**
 * Fixed-Timestep Game Loop.
 * Update und Render laufen entkoppelt – stabile Physik unabhängig von der FPS.
 */
public class GameLoop {

    private final Updatable updatable;
    private volatile boolean running = false;
    private Thread loopThread;

    public GameLoop(Updatable updatable) {
        this.updatable = updatable;
    }

    public void start() {
        running = true;
        loopThread = new Thread(this::loop, "GameLoop");
        loopThread.setDaemon(true);
        loopThread.start();
    }

    public void stop() {
        running = false;
    }

    private void loop() {
        long lastTime = System.currentTimeMillis();

        while (running) {
            long now     = System.currentTimeMillis();
            long elapsed = now - lastTime;
            lastTime = now;

            updatable.update(elapsed);
            updatable.render();

            long sleep = Constants.FRAME_TIME_MS - (System.currentTimeMillis() - now);
            if (sleep > 0) {
                try { Thread.sleep(sleep); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }
    }
}
