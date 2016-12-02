package com.example.stevennl.tastysnake.model;

/**
 * Coordinate(position) in 2D plane.
 */
public class Pos implements Cloneable {
    private int x;
    private int y;
    private static int dx[] = {-1, 0, 1, 0, 0};
    private static int dy[] = {0, 1, 0, -1, 0};
    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Pos clone() {
        return new Pos(x, y);
    }
    public boolean equals(Pos a) {
        return x == a.getX() && y == a.getY();
    }
    public Pos to(Direction k) {
        return new Pos(x + dx[k.ordinal()], y + dy[k.ordinal()]);
    }
    public Direction dirTo(Pos a) {
        for (int i = 0; i < 4; i ++) {
            if (a.to(Direction.values()[i]).equals(this))
                return Direction.values()[i];
        }
        return Direction.NONE;
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
