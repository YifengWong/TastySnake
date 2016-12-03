package com.example.stevennl.tastysnake;

import android.graphics.Color;

public class Constants {
    public static final String BLUETOOTH_SERVICE_NAME = "TastySnake";
    public static final String BLUETOOTH_UUID_STR = "aa87265a-1309-4d8b-8d8f-47390b47f336";
    public static final int BLUETOOTH_TEST_DATA_SIZE = (int)(1000);  // bytes
    public static final int BLUETOOTH_DISCOVERABLE_TIME = 180;  // seconds
    public static final int BLUETOOTH_DISCOVER_TIME = 5000;  // milliseconds

    public static final int MAP_ROW = 50;
    public static final int MAP_COL = 30;

    public static final int COLOR_FOOD_LENGTHEN = Color.GREEN;
    public static final int COLOR_FOOD_SHORTEN = Color.BLACK;
    public static final int COLOR_SNAKE_PLAYER = Color.RED;
    public static final int COLOR_SNAKE_RIVAL = Color.CYAN;
    public static final int COLOR_EMPTY_POINT = Color.WHITE;
    public static final int COLOR_MAP_BG = Color.WHITE;

    public static final int INTERVAL_DRAW = 30;  // milliseconds
    public static final int INTERVAL_MOVE = 150;  // milliseconds

    public static final float GRAVITY_SENSITIVITY = 0.3f;  // lower(zero at least) -> more sensitive
}
