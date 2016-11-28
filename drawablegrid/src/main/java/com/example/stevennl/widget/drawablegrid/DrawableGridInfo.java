package com.example.stevennl.widget.drawablegrid;

import android.graphics.Color;

/**
 * The information stored in each grid.
 */
public class DrawableGridInfo {
    public int color = Color.TRANSPARENT;
    public Type type = Type.BLANK;

    public DrawableGridInfo() {
    }

    public enum Type {
        BLANK,
        FOOD,
        HEAD_L,
        HEAD_U,
        HEAD_R,
        HEAD_D,
        BODY_HOR,
        BODY_VER,
        BODY_L_U,
        BODY_L_D,
        BODY_R_U,
        BODY_R_D
    }
}
