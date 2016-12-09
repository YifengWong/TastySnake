package com.example.stevennl.tastysnake.model;

import android.graphics.Color;

import com.example.stevennl.tastysnake.Config;

import java.util.ArrayList;

/**
 * Game snake.
 * Author: WTY
 */
public class Snake {
    private ArrayList<Pos> bodies = new ArrayList<>();
    private ArrayList<Point.Type> types = new ArrayList<>();
    private Direction direc;
    private Map map;
    private int row;
    private int col;
    private int color;

    /**
     * Type of the snake.
     */
    public enum Type {
        SERVER,
        CLIENT
    }

    /**
     * Result after snake moves.
     */
    public enum MoveResult {
        OUT,
        SUICIDE,
        HIT_ENEMY,
        SUC,
    }

    /**
     * Initialize.
     *
     * @param type The type of the snake
     * @param map The game map
     * @param color The color of the snake
     */
    public Snake(Type type, Map map, int color) {
        switch (type) {
            case SERVER:
                direc = Direction.RIGHT;
                for (int i = 3; i > 0; --i) {
                    bodies.add(new Pos(10, 6 + i));
                }
                break;
            case CLIENT:
                direc = Direction.RIGHT;
                for (int i = 3; i > 0; --i) {
                    bodies.add(new Pos(40, 6 + i));
                }
                break;
            default:
                break;
        }
        this.map = map;
        this.color = color;
        this.row = map.getRowCount();
        this.col = map.getColCount();
        genType();
        for (int i = 0; i < bodies.size(); i ++) {
            map.setPoint(bodies.get(i), new Point(color, this.types.get(i)));
        }
    }

    /**
     * Move the snake at a given direction.
     */
    public MoveResult move(Direction order) {
        Pos head = bodies.get(0);
        if (order == Direction.NONE || order.ordinal() == (direc.ordinal() + 2) % 4) {
            bodies.add(0, head.to(direc));
        } else {
            bodies.add(0, head.to(order));
            direc = order;
        }
        MoveResult moveResult = calMoveResult();
        Pos tail = bodies.get(bodies.size() - 1);
        Point.Type newHeadType = map.getPoint(bodies.get(0)).getType();
        if (newHeadType == Point.Type.FOOD_LENGTHEN) {
            // Do nothing
        } else if (newHeadType == Point.Type.FOOD_SHORTEN) {
            bodies.remove(bodies.size() - 1);
            map.getPoint(tail).makeEmpty();
            if (bodies.size() > 2) {
                tail = bodies.get(bodies.size() - 1);
                bodies.remove(bodies.size() - 1);
                map.getPoint(tail).makeEmpty();
            }
        } else {
            bodies.remove(bodies.size() - 1);
            map.getPoint(tail).makeEmpty();
        }
        genType();
        map.setPoint(bodies.get(0), new Point(color, types.get(0)));
        map.setPoint(bodies.get(1), new Point(color, types.get(1)));
        return moveResult;
    }

    /**
     * Calculate snake's move result.
     */
    private MoveResult calMoveResult() {
        Pos head = bodies.get(0);
        if (head.getX() >= row || head.getX() < 0 || head.getY() >= col || head.getY() < 0) return MoveResult.OUT;
        for (int i = 0; i < bodies.size(); i ++)
            for (int j = i + 1; j < bodies.size(); j ++)
                if (bodies.get(i).getX() == bodies.get(j).getX() && bodies.get(i).getY() == bodies.get(j).getY())
                    return MoveResult.SUICIDE;
        int headColor = map.getPoint(head).getColor();
        if ((headColor == Config.COLOR_SNAKE_ENEMY || headColor == Config.COLOR_SNAKE_MY) && headColor != color)
            return MoveResult.HIT_ENEMY;
        return MoveResult.SUC;
    }

    /**
     * Generate body types list.
     */
    private void genType() {
        types.clear();
        switch (direc) {
            case UP:
                types.add(0, Point.Type.HEAD_U);
                break;
            case DOWN:
                types.add(0, Point.Type.HEAD_D);
                break;
            case LEFT:
                types.add(0, Point.Type.HEAD_L);
                break;
            case RIGHT:
                types.add(0, Point.Type.HEAD_R);
                break;
        }
        for (int i = 1; i < bodies.size() - 1; i ++) {
            Pos pre = bodies.get(i - 1);
            Pos succ = bodies.get(i + 1);
            Pos now = bodies.get(i);
            if (pre.dirTo(now) == Direction.LEFT && now.dirTo(succ) == Direction.LEFT)
                types.add(Point.Type.BODY_HOR);
            if (pre.dirTo(now) == Direction.RIGHT && now.dirTo(succ) == Direction.RIGHT)
                types.add(Point.Type.BODY_HOR);
            if (pre.dirTo(now) == Direction.UP && now.dirTo(succ) == Direction.UP)
                types.add(Point.Type.BODY_VER);
            if (pre.dirTo(now) == Direction.DOWN && now.dirTo(succ) == Direction.DOWN)
                types.add(Point.Type.BODY_VER);
            if ((pre.dirTo(now) == Direction.UP && succ.dirTo(now) == Direction.LEFT)
                    || (pre.dirTo(now) == Direction.LEFT && succ.dirTo(now) == Direction.UP))
                types.add(Point.Type.BODY_L_U);
            if ((pre.dirTo(now) == Direction.DOWN && succ.dirTo(now) == Direction.LEFT)
                    || (pre.dirTo(now) == Direction.LEFT && succ.dirTo(now) == Direction.DOWN))
                types.add(Point.Type.BODY_L_D);
            if ((pre.dirTo(now) == Direction.UP && succ.dirTo(now) == Direction.RIGHT)
                    || (pre.dirTo(now) == Direction.RIGHT && succ.dirTo(now) == Direction.UP))
                types.add(Point.Type.BODY_R_U);
            if ((pre.dirTo(now) == Direction.DOWN && succ.dirTo(now) == Direction.RIGHT)
                    || (pre.dirTo(now) == Direction.RIGHT && succ.dirTo(now) == Direction.DOWN))
                types.add(Point.Type.BODY_R_D);
        }
        if (bodies.size() >= 2) {
            Pos last = bodies.get(bodies.size() - 1);
            Pos butLast = bodies.get(bodies.size() - 2);
            if (last.dirTo(butLast).ordinal() % 2 == 1)
                types.add(Point.Type.BODY_HOR);
            else
                types.add(Point.Type.BODY_VER);
        }
    }
}
