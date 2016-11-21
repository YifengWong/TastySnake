package com.example.stevennl.tastysnake.util;

import android.content.Context;
import android.widget.Toast;

public class CommonUtil {
    private static final String TAG = "CommonUtil";

    /**
     * Show a Toast message
     * @param context the context
     * @param strId the string resource id
     */
    public static void showToast(Context context, int strId) {
        showToast(context, context.getString(strId));
    }

    /**
     * Show a Toast message
     * @param context the context
     * @param str the string content
     */
    public static void showToast(Context context, String str) {
        if (context != null) {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }
}
