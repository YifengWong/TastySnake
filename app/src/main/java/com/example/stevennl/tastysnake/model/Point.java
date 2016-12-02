package com.example.stevennl.tastysnake.model;

import com.example.stevennl.tastysnake.Constants;

/**
 * Point on the game map.
 */
public class Point {
    private int color;
    private Type type;

    public enum Type {
        BLANK,
        FOOD_LENGTHEN,
        FOOD_SHORTEN,
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
        this(Constants.COLOR_EMPTY_POINT, Type.BLANK);
    }

    public Point(int color, Type type) {
        this.color = color;
        this.type = type;
    }

    public void makeEmpty() {
        color = Constants.COLOR_EMPTY_POINT;
        type = Type.BLANK;
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
