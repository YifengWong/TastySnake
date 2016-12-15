package com.example.stevennl.tastysnake.controller.game;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import com.example.stevennl.tastysnake.base.SingleFragmentActivity;

/**
 * Activity controlling the game.
 * Author: LCY
 */
public class GameActivity extends SingleFragmentActivity {
    private static final String TAG = "GameActivity";

    @Override
    protected Fragment createFragment() {
        return new HomeFragment();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(getFrameLayoutId());
                String className = fragment.getClass().getName();
                if (className.equals(HomeFragment.class.getName())) {
                    ((HomeFragment)fragment).onBackPressed();
                } else if (className.equals(ConnectFragment.class.getName())) {
                    ((ConnectFragment)fragment).onBackPressed();
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
