package com.example.stevennl.tastysnake;

/**
 * Game configuration.
 */
public class Config {
    public static final int MAP_ROW = 25;
    public static final int MAP_COL = 49;

    public static final int FREQUENCY_DRAW = 20;  // ms
    public static final int FREQUENCY_MOVE = 80;  // ms
    public static final int FREQUENCY_FOOD = 500;  // ms

    public static final int DELAY_HOME_FRAG = 600;  // ms
    public static final int DELAY_HOME_FINISH = 200;  // ms
    public static final int DELAY_HOME_CLICKME = 3000;  // ms
    public static final int DELAY_ANALYSIS_FRAG = 1000;  // ms
    public static final int DELAY_CONNECT = 1000;  // ms
    public static final int DELAY_BATTLE_FRAG = 1000;  // ms
    public static final int DELAY_ROLE_SWITCH_INFO = 1000;  // ms

    public static final int DURATION_ATTACK = 10;  // s
    public static final int DURATION_GAME_PREPARE = 3;  // s
    public static final int DURATION_SNAKE_ANIM = 400;  // ms
    public static final int DURATION_SHOW_PRETTY = 1000;  // ms

    public static final float GRAVITY_SENSITIVITY = 0.1f;  // lower(zero at least) -> more sensitive

    public static final int INIT_SNAKE_HOR_OFFSET = 4;
    public static final int INIT_SNAKE_LENGTH = 3;

    public static final String BLUETOOTH_SERVICE_NAME = "TastySnake";
    public static final String BLUETOOTH_UUID_STR = "aa87265a-1309-4d8b-8d8f-47390b47f336";
    public static final int BLUETOOTH_TEST_DATA_SIZE = 500;  // bytes
    public static final int BLUETOOTH_DISCOVERABLE_TIME = 180;  // s
    public static final int BLUETOOTH_DISCOVER_TIME = 5000;  // ms

    public static final String URL_SERVER = "http://192.168.199.105/api/testReq";
    public static final int REQ_TIMEOUT = 3000;

    public enum ThemeType {LIGHT, DARK}
    public static ThemeType theme = ThemeType.LIGHT;

    public static int COLOR_FOOD_LENGTHEN;
    public static int COLOR_FOOD_SHORTEN;
    public static int COLOR_SNAKE_MY;
    public static int COLOR_SNAKE_ENEMY;
    public static int COLOR_EMPTY_POINT;
    public static int COLOR_MAP_BG;
}
