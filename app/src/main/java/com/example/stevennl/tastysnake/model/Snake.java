package com.example.stevennl.tastysnake.model;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * XXX
 * Author: XXX
 */
public class Snake {
    public ArrayList<Pos> body = new ArrayList<>();
    public ArrayList<Point.Type> type = new ArrayList<>();
    Direction orientation;
    Map map;
    int row, column;
    int color;
    static final Point EMPTY = new Point(Color.TRANSPARENT, Point.Type.BLANK);
    public Snake(int type, Map map) {
        switch (type) {
            case 0:
                color = Color.rgb(204, 0, 0);
                orientation = Direction.RIGHT;
                for (int i = 10; i >= 0; i --) {
                    body.add(new Pos(0, i));
                }
                break;
            default:
        }
        this.map = map;
        this.row = map.getRowCount();
        this.column = map.getColCount();
        genType();
        for (int i = 0; i < body.size(); i ++)
            map.setPoint(body.get(i), new Point(color, this.type.get(i)));
    }
    public void extend() {

    }
    public void genType() {
        type.clear();
        switch (orientation) {
            case UP:
                type.add(0, Point.Type.HEAD_U);
                break;
            case DOWN:
                type.add(0, Point.Type.HEAD_D);
                break;
            case LEFT:
                type.add(0, Point.Type.HEAD_L);
                break;
            case RIGHT:
                type.add(0, Point.Type.HEAD_R);
                break;
        }
        for (int i = 1; i < body.size() - 1; i ++) {
            Pos pre = body.get(i - 1);
            Pos succ = body.get(i + 1);
            Pos now = body.get(i);
            if (pre.dirTo(now) == Direction.LEFT && now.dirTo(succ) == Direction.LEFT)
                type.add(Point.Type.BODY_HOR);
            if (pre.dirTo(now) == Direction.RIGHT && now.dirTo(succ) == Direction.RIGHT)
                type.add(Point.Type.BODY_HOR);
            if (pre.dirTo(now) == Direction.UP && now.dirTo(succ) == Direction.UP)
                type.add(Point.Type.BODY_VER);
            if (pre.dirTo(now) == Direction.DOWN && now.dirTo(succ) == Direction.DOWN)
                type.add(Point.Type.BODY_VER);
            if ((pre.dirTo(now) == Direction.UP && succ.dirTo(now) == Direction.LEFT)
                    || (pre.dirTo(now) == Direction.LEFT && succ.dirTo(now) == Direction.UP))
                type.add(Point.Type.BODY_L_U);
            if ((pre.dirTo(now) == Direction.DOWN && succ.dirTo(now) == Direction.LEFT)
                    || (pre.dirTo(now) == Direction.LEFT && succ.dirTo(now) == Direction.DOWN))
                type.add(Point.Type.BODY_L_D);
            if ((pre.dirTo(now) == Direction.UP && succ.dirTo(now) == Direction.RIGHT)
                    || (pre.dirTo(now) == Direction.RIGHT && succ.dirTo(now) == Direction.UP))
                type.add(Point.Type.BODY_R_U);
            if ((pre.dirTo(now) == Direction.DOWN && succ.dirTo(now) == Direction.RIGHT)
                    || (pre.dirTo(now) == Direction.RIGHT && succ.dirTo(now) == Direction.DOWN))
                type.add(Point.Type.BODY_R_D);
        }
        Pos last = body.get(body.size() - 1);
        Pos butLast = body.get(body.size() - 2);
        if (last.dirTo(butLast).ordinal() % 2 == 1)
            type.add(Point.Type.BODY_HOR);
        else
            type.add(Point.Type.BODY_VER);
    }
    public boolean move(Direction order) {
        Pos head = body.get(0);
        if (order == Direction.NONE || order.ordinal() == (orientation.ordinal() + 2) % 4) {
            body.add(0, head.to(orientation));
        } else {
            body.add(0, head.to(order));
            orientation = order;
        }
        Pos tail = body.get(body.size() - 1);
        if (map.getPoint(body.get(0)).getType() == Point.Type.FOOD) {
            if (map.isWeather()) {
                if (body.size() <= 2) return false;
                body.remove(body.size() - 1);
                map.setPoint(tail, EMPTY);
                tail = body.get(body.size() - 1);
                body.remove(body.size() - 1);
                map.setPoint(tail, EMPTY);
            } else {
                //Do Nothing
            }
        } else {
            body.remove(body.size() - 1);
            map.setPoint(tail, EMPTY);
        }
        genType();

        map.setPoint(body.get(0), new Point(color, type.get(0)));
        map.setPoint(body.get(1), new Point(color, type.get(1)));
        checkHead();
        return checkOut();
    }
    void checkHead() {

    }
    public boolean checkOut() {
        Pos head = body.get(0);
        if (head.getX() >= row || head.getX() < 0 || head.getY() >= column || head.getY() < 0) return false;
        for (int i = 0; i < body.size(); i ++)
            for (int j = i + 1; j < body.size(); j ++)
                if (body.get(i).getX() == body.get(j).getX() && body.get(i).getY() == body.get(j).getY())
                    return false;
        return true;
    }
}
