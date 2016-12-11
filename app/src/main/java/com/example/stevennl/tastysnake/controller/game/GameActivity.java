package com.example.stevennl.tastysnake.controller.game;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.example.stevennl.tastysnake.base.SingleFragmentActivity;
import com.example.stevennl.tastysnake.util.CommonUtil;

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                FragmentManager manager = getSupportFragmentManager();
                Fragment f = manager.findFragmentById(getFrameLayoutId());
                if (f.getClass().getName().equals(HomeFragment.class.getName())) {
                    finish();
                } else {
                    replaceFragment(new HomeFragment(), true);
                }
                break;
            default:
                break;
        }
        return false;
    }
}
