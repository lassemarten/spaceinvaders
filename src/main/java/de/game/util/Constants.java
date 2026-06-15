package main.java.de.game.util;

/**
 * Zentrale Spielkonstanten – alle Magic Numbers an einem Ort.
 */
public final class Constants {

    private Constants() {}

    // Fenster
    public static final int SCREEN_WIDTH  = 900;
    public static final int SCREEN_HEIGHT = 675;
    public static final String TITLE      = "Space Invaders";
    public static final int TARGET_FPS    = 60;
    public static final int FRAME_TIME_MS = 1000 / TARGET_FPS;

    // Spieler
    public static final int PLAYER_WIDTH   = 64;
    public static final int PLAYER_HEIGHT  = 220;
    public static final int PLAYER_SPEED   = 5;
    public static final int PLAYER_Y       = SCREEN_HEIGHT - 70;

    // Dash
    public static final int DASH_COOLDOWN  = 200;
    public static final int DASH_Y = SCREEN_HEIGHT - 671;
    public static final int DASH_X = SCREEN_WIDTH - 80;

    // Kugeln
    public static final int BULLET_WIDTH        = 18;
    public static final int BULLET_HEIGHT       = 20;
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
    public static final int SCORE_PER_BULLET_ON_BULLET = 3;

    // Dateipfade
    public static final String TECTUR_PLAYER = "/sprites/spaceShip.png";
    public static final String TECTUR_INVADER_GREEN = "/sprites/invaderGreen.png";
    public static final String TECTUR_INVADER_RED = "/sprites/invaderRed.png";
    public static final String TECTUR_INVADER_BLUE = "/sprites/invaderBlue.png";
    public static final String TECTUR_BACKGROUND = "/sprites/spaceBackground.jpg";
}
