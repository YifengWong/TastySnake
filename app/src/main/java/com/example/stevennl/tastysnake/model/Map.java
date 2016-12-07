package com.example.stevennl.tastysnake.model;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.util.CommonUtil;

import java.util.Random;

/**
 * Game map.
 * Author: WTY
 */
public class Map {
    private Point[][] content;
    private int row;
    private int col;

    /**
     * Return a game map with specific row and column amount.
     */
    public static Map gameMap() {
        return new Map(Config.MAP_ROW, Config.MAP_COL);
    }

    /**
     * Initialize.
     *
     * @param row the row amount of the map
     * @param col the column amount of the map
     */
    public Map(int row, int col) {
        this.row = row;
        this.col = col;
        content = new Point[row][col];
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                content[i][j] = new Point();
            }
        }
    }

    /**
     * Create food randomly.
     *
     * @param lengthen True if the food will lengthen the snake who eats it, false shorten
     * @return The position of the created food
     */
    public Pos createFood(boolean lengthen) {
        int x, y, row = getRowCount(), col = getColCount();
        do {
            x = CommonUtil.randInt(row);
            y = CommonUtil.randInt(col);
        } while (content[x][y].getType() != Point.Type.BLANK);
        createFood(x, y, lengthen);
        return new Pos(x, y);
    }

    /**
     * Create food at a given position.
     *
     * @param x The row number of the food
     * @param y The column number of the food
     * @param lengthen True if the food will lengthen the snake who eats it, false shorten
     */
    public void createFood(int x, int y, boolean lengthen) {
        content[x][y].setColor(lengthen ? Config.COLOR_FOOD_LENGTHEN : Config.COLOR_FOOD_SHORTEN);
        content[x][y].setType(lengthen ? Point.Type.FOOD_LENGTHEN : Point.Type.FOOD_SHORTEN);
    }

    /**
     * Set a point on the map.
     *
     * @param p The position of the new point
     * @param c The new point
     */
    public void setPoint(Pos p, Point c) {
        if (isValid(p.getX(), p.getY())) {
            content[p.getX()][p.getY()] = c;
        }
    }

    /**
     * Return a point on the map.
     *
     * @param p The position of the point
     */
    public Point getPoint(Pos p) {
        if (!isValid(p.getX(), p.getY()))  {
            return new Point();
        }
        return content[p.getX()][p.getY()];
    }

    /**
     * Return a point on the map.
     *
     * @param x The row number of the point
     * @param y The column number of the point
     */
    public Point getPoint(int x, int y) {
        if (!isValid(x, y))  {
            return new Point();
        }
        return content[x][y];
    }

    /**
     * Return the amount of rows.
     */
    public int getRowCount() {
        return row;
    }

    /**
     * Return the amount of columns.
     */
    public int getColCount() {
        return col;
    }

    /**
     * Return true if a position with coordinate x and y is valid, false otherwise.
     */
    private boolean isValid(int x, int y) {
        return x >= 0 && x < row && y >= 0 && y < col;
    }
}
