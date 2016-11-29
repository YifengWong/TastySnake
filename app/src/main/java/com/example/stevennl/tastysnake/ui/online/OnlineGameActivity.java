package com.example.stevennl.tastysnake.ui.online;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.base.SingleFragmentActivity;

public class OnlineGameActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ConnectFragment();
    }
}
