package com.example.stevennl.tastysnake.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;

import java.util.Random;

/**
 * Commonly used methods.
 */
public class CommonUtil {
    private static final String TAG = "CommonUtil";
    private static final Random random = new Random();

    /**
     * Show a Toast message
     * @param context The context
     * @param strId The string resource id
     */
    public static void showToast(Context context, int strId) {
        showToast(context, context.getString(strId));
    }

    /**
     * Show a Toast message
     * @param context The context
     * @param str The string content
     */
    public static void showToast(Context context, String str) {
        if (context != null) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Return a random integer in [0, max).
     */
    public static int randInt(int max) {
        return random.nextInt(max);
    }

    /**
     * Return role string (attacker or defender).
     *
     * @param context The context
     * @param attack If true, return attacker string, otherwise return defender string.
     */
    public static String getAttackStr(Context context, boolean attack) {
        return attack ? context.getString(R.string.you_attacker) : context.getString(R.string.you_defender);
    }
}
