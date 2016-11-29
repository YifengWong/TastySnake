package com.example.stevennl.tastysnake.model;

import java.util.ArrayList;

/**
 * XXX
 * Author: XXX
 */
public class Snake {
    public ArrayList<Pair> body = new ArrayList<>();
    Direction orientation;
    int row, column;
    public Snake(int type, int row, int column) {
        switch (type) {
            case 0:
                orientation = Direction.RIGHT;
                for (int i = 10; i >= 0; i --) {
                    body.add(new Pair(0, i));
                }
                break;
            default:
        }
        this.row = row;
        this.column = column;
    }
    public void extend() {

    }
    public void move(Direction order) {
        Pair head = body.get(0);
        switch (orientation) {
            case UP:
                if (order == Direction.NONE) {
                    body.add(0, head.toUP());
                }
                if (order == Direction.LEFT) {
                    body.add(0, head.toLEFT());
                }
                if (order == Direction.RIGHT) {
                    body.add(0, head.toRIGHT());
                }
                break;
            case DOWN:
                if (order == Direction.NONE) {
                    body.add(0, head.toDOWN());
                }
                if (order == Direction.LEFT) {
                    body.add(0, head.toRIGHT());
                }
                if (order == Direction.RIGHT) {
                    body.add(0, head.toLEFT());
                }
                break;
            case LEFT:
                if (order == Direction.NONE) {
                    body.add(0, head.toLEFT());
                }
                if (order == Direction.LEFT) {
                    body.add(0, head.toDOWN());
                }
                if (order == Direction.RIGHT) {
                    body.add(0, head.toUP());
                }
                break;
            case RIGHT:
                if (order == Direction.NONE) {
                    body.add(0, head.toRIGHT());
                }
                if (order == Direction.LEFT) {
                    body.add(0, head.toUP());
                }
                if (order == Direction.RIGHT) {
                    body.add(0, head.toDOWN());
                }
                break;
        }
        body.remove(body.size() - 1);
    }
    public boolean canMove() {
        Pair head = body.get(0);
        if (head.getX() >= row || head.getX() < 0 || head.getY() >= column || head.getX() < 0) return false;
        for (Pair i : body)
            for (Pair j : body)
                if (i.getX() == j.getX() && i.getY() == j.getY())
                    return false;
        return true;
    }
}
