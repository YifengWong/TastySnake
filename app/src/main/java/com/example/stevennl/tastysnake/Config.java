package com.example.stevennl.tastysnake;

import android.graphics.Color;

/**
 * Game configuration.
 */
public class Config {
    public static final String BLUETOOTH_SERVICE_NAME = "TastySnake";
    public static final String BLUETOOTH_UUID_STR = "aa87265a-1309-4d8b-8d8f-47390b47f336";
    public static final int BLUETOOTH_TEST_DATA_SIZE = 500;  // bytes
    public static final int BLUETOOTH_DISCOVERABLE_TIME = 180;  // seconds
    public static final int BLUETOOTH_DISCOVER_TIME = 5000;  // milliseconds

    public static final int MAP_ROW = 50;
    public static final int MAP_COL = 30;

    public static final int INTERVAL_DRAW = 30;  // milliseconds
    public static final int INTERVAL_MOVE = 175;  // milliseconds
    public static final int INTERVAL_FOOD = 3000;  // milliseconds

    public static final int COLOR_FOOD_LENGTHEN = Color.GREEN;
    public static final int COLOR_FOOD_SHORTEN = Color.BLACK;
    public static final int COLOR_SNAKE_SERVER = Color.RED;
    public static final int COLOR_SNAKE_CLIENT = Color.BLUE;
    public static final int COLOR_EMPTY_POINT = Color.WHITE;
    public static final int COLOR_MAP_BG = Color.WHITE;

    public static final float GRAVITY_SENSITIVITY = 0.1f;  // lower(zero at least) -> more sensitive
}
