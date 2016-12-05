package com.example.stevennl.tastysnake.controller.game;

import android.support.v4.app.Fragment;

import com.example.stevennl.tastysnake.base.SingleFragmentActivity;

/**
 * Activity controlling the game.
 * Author: LCY
 */
public class GameActivity extends SingleFragmentActivity {
    private static final String TAG = "GameActivity";

    /**
     * Return the first fragment shown in the activity.
     */
    @Override
    protected Fragment createFragment() {
        return new HomeFragment();
    }
}
