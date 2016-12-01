package com.example.stevennl.tastysnake.model;

import java.nio.channels.Pipe;

/**
 * Created by moret on 2016/11/29.
 */

public class Pair implements Cloneable {
    int x, y;
    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public Pair clone() {
        return new Pair(x, y);
    }
    public Pair toUP() {
        return new Pair(x - 1, y);
    }
    public Pair toRIGHT() {
        return new Pair(x, y + 1);
    }
    public Pair toDOWN() {
        return new Pair(x + 1, y);
    }
    public Pair toLEFT() {
        return new Pair(x, y - 1);
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean onLeftOf(Pair a) {
        return x == a.getX() && y == a.getY() - 1;
    }
    public boolean onRightOf(Pair a) {
        return x == a.getX() && y == a.getY() + 1;
    }
    public boolean onUpOf(Pair a) {
        return x == a.getX() - 1 && y == a.getY();
    }
    public boolean onDownOf(Pair a) {
        return x == a.getX() + 1 && y == a.getY();
    }
}
