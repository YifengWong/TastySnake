package com.example.stevennl.tastysnake.model;

import android.graphics.Color;

/**
 * Point on the game map.
 */
public class Point {
    private int color = Color.TRANSPARENT;
    private Type type = Type.BLANK;

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

    public Point() {
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
