package com.example.stevennl.tastysnake;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

public class TastySnakeApplication extends Application {
    private static final String TAG = "TastySnakeApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        refreshGameColor(this);
    }

    /**
     * Refresh game color according to resources.
     *
     * @param context The context
     */
    public static void refreshGameColor(Context context) {
        Resources res = context.getResources();
        Config.COLOR_FOOD_LENGTHEN = res.getColor(R.color.foodLengthen);
        Config.COLOR_FOOD_SHORTEN = res.getColor(R.color.foodShorten);
        Config.COLOR_SNAKE_MY = res.getColor(R.color.snakeMy);
        Config.COLOR_SNAKE_ENEMY = res.getColor(R.color.snakeEnemy);
        Config.COLOR_EMPTY_POINT = res.getColor(R.color.emptyPoint);
        Config.COLOR_MAP_BG = res.getColor(R.color.mapBg);
    }
}
