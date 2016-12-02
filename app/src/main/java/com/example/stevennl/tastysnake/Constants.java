package com.example.stevennl.tastysnake;

import android.graphics.Color;

public class Constants {
    public static final String BLUETOOTH_SERVICE_NAME = "TastySnake";
    public static final String BLUETOOTH_UUID_STR = "aa87265a-1309-4d8b-8d8f-47390b47f336";
    public static final int BLUETOOTH_TEST_DATA_SIZE = (int)(1000);  // bytes
    public static final int DEVICE_DISCOVERABLE_TIME = 180;  // seconds
    public static final int DEVICE_DISCOVER_TIME = 5000;  // milliseconds

    public static final int MAP_ROW = 50;
    public static final int MAP_COL = 30;

    public static final int FOOD_LENGTHEN_COLOR = Color.GREEN;
    public static final int FOOD_SHORTEN_COLOR = Color.BLACK;
    public static final int SNAKE_PLAYER_COLOR = Color.RED;
    public static final int SNAKE_RIVAL_COLOR = Color.CYAN;
    public static final int EMPTY_POINT_COLOR = Color.WHITE;
}
