package com.example.stevennl.tastysnake.model;

/**
 * Coordinate(position) in 2D plane.
 */
public class Pos implements Cloneable {
    private int x;
    private int y;

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Pos clone() {
        return new Pos(x, y);
    }
    public Pos toUP() {
        return new Pos(x - 1, y);
    }
    public Pos toRIGHT() {
        return new Pos(x, y + 1);
    }
    public Pos toDOWN() {
        return new Pos(x + 1, y);
    }
    public Pos toLEFT() {
        return new Pos(x, y - 1);
    }
    public boolean onLeftOf(Pos a) {
        return x == a.getX() && y == a.getY() - 1;
    }
    public boolean onRightOf(Pos a) {
        return x == a.getX() && y == a.getY() + 1;
    }
    public boolean onUpOf(Pos a) {
        return x == a.getX() - 1 && y == a.getY();
    }
    public boolean onDownOf(Pos a) {
        return x == a.getX() + 1 && y == a.getY();
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
