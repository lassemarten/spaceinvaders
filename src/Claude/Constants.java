package de.game.util;

/**
 * Zentrale Spielkonstanten – alle Magic Numbers an einem Ort.
 */
public final class Constants {

    private Constants() {}

    // Fenster
    public static final int SCREEN_WIDTH  = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final String TITLE      = "Space Invaders";
    public static final int TARGET_FPS    = 60;
    public static final int FRAME_TIME_MS = 1000 / TARGET_FPS;

    // Spieler
    public static final int PLAYER_WIDTH   = 40;
    public static final int PLAYER_HEIGHT  = 30;
    public static final int PLAYER_SPEED   = 5;
    public static final int PLAYER_Y       = SCREEN_HEIGHT - 70;

    // Kugeln
    public static final int BULLET_WIDTH        = 6;
    public static final int BULLET_HEIGHT       = 15;
    public static final int BULLET_SPEED        = 8;
    public static final int INVADER_BULLET_SPEED = 4;

    // Invasoren
    public static final int INVADER_ROWS        = 3;
    public static final int INVADER_COLS        = 10;
    public static final int INVADER_WIDTH       = 40;
    public static final int INVADER_HEIGHT      = 30;
    public static final int INVADER_H_GAP       = 15;
    public static final int INVADER_V_GAP       = 15;
    public static final int INVADER_START_X     = 80;
    public static final int INVADER_START_Y     = 80;
    public static final int INVADER_STEP_DOWN   = 20;
    public static final int INVADER_SPEED       = 2;
    public static final int INVADER_SHOOT_TICKS = 60;

    // Punkte
    public static final int SCORE_PER_KILL = 10;
}
