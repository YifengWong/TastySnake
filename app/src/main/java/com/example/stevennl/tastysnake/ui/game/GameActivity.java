package com.example.stevennl.tastysnake.ui.game;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.example.stevennl.tastysnake.base.SingleFragmentActivity;

/**
 * Activity controlling the game.
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
