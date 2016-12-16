package com.example.stevennl.tastysnake.base;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;

/**
 * Activity that will load custom theme when starting.
 * Author: LCY
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTheme();
    }

    /**
     * Update theme and colors.
     */
    private void updateTheme() {
        switch (Config.theme) {
            case LIGHT:
                setTheme(R.style.AppLightTheme);
                break;
            case DARK:
                setTheme(R.style.AppDarkTheme);
                break;
            default:
                break;
        }
        TypedArray arr = getTheme().obtainStyledAttributes(R.styleable.ThemeAttr);
        Config.COLOR_FOOD_LENGTHEN = arr.getColor(R.styleable.ThemeAttr_colorFoodLengthen, Color.TRANSPARENT);
        Config.COLOR_FOOD_SHORTEN = arr.getColor(R.styleable.ThemeAttr_colorFoodShorten, Color.TRANSPARENT);
        Config.COLOR_SNAKE_MY = arr.getColor(R.styleable.ThemeAttr_colorSnakeMy, Color.TRANSPARENT);
        Config.COLOR_SNAKE_ENEMY = arr.getColor(R.styleable.ThemeAttr_colorSnakeEnemy, Color.TRANSPARENT);
        Config.COLOR_EMPTY_POINT = arr.getColor(R.styleable.ThemeAttr_colorEmptyPoint, Color.TRANSPARENT);
        Config.COLOR_MAP_BG = arr.getColor(R.styleable.ThemeAttr_colorMapBg, Color.TRANSPARENT);
        arr.recycle();
    }
}
