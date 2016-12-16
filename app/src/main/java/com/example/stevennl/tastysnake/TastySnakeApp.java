package com.example.stevennl.tastysnake;

import android.app.Application;

import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.SharedPrefUtil;

public class TastySnakeApp extends Application {
    private static final String TAG = "TastySnakeApp";

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefUtil.loadThemeType(this);
    }
}
