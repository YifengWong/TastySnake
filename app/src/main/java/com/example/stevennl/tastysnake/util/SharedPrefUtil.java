package com.example.stevennl.tastysnake.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.stevennl.tastysnake.Config;

/**
 * Manage operations for {@link SharedPreferences}.
 * Author: LCY
 */
public class SharedPrefUtil {
    private static final String TAG = "SharedPrefUtil";
    private static final String PREF_APP = "TastySnake_shared_pref";
    private static final String PREF_KEY_THEME_TYPE = "theme_type";

    /**
     * Save current theme type.
     *
     * @param context The context
     */
    public static void saveThemeType(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);
        sp.edit().putInt(PREF_KEY_THEME_TYPE, Config.theme.ordinal()).apply();
    }

    /**
     * Load current theme type.
     *
     * @param context The context
     */
    public static void loadThemeType(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE);
        int type = sp.getInt(PREF_KEY_THEME_TYPE, Config.ThemeType.LIGHT.ordinal());
        Config.theme = Config.ThemeType.values()[type];
    }
}
