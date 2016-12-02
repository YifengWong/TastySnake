package com.example.stevennl.tastysnake.model;

import com.example.stevennl.tastysnake.Constants;

import java.util.Random;

/**
 * Game map.
 */
public class Map {
    private Point[][] content;

    /**
     * Initialize.
     *
     * @param row the row amount of the map
     * @param col the column amount of the map
     */
    public Map(int row, int col) {
        content = new Point[row][col];
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < col; ++j) {
                content[i][j] = new Point();
            }
        }
    }

    /**
     * Create food on the map.
     *
     * @param lengthen True if the food will lengthen the snake who eats it, false shorten
     */
    public void createFood(boolean lengthen) {
        int row = getRowCount(), col = getColCount();
        int x, y;
        Random rand = new Random();
        do {
            x = rand.nextInt(row);
            y = rand.nextInt(col);
        } while (content[x][y].getType() != Point.Type.BLANK);
        content[x][y].setColor(lengthen ? Constants.COLOR_FOOD_LENGTHEN : Constants.COLOR_FOOD_SHORTEN);
        content[x][y].setType(lengthen ? Point.Type.FOOD_LENGTHEN : Point.Type.FOOD_SHORTEN);
    }

    /**
     * Set a point on the map.
     *
     * @param p The position of the new point
     * @param c The new point
     */
    public void setPoint(Pos p, Point c) {
        content[p.getX()][p.getY()] = c;
    }

    /**
     * Return a point on the map.
     *
     * @param p The position of the point
     */
    public Point getPoint(Pos p) {
        return content[p.getX()][p.getY()];
    }

    /**
     * Return a point on the map.
     *
     * @param x The row number of the point
     * @param y The column number of the point
     */
    public Point getPoint(int x, int y) {
        return content[x][y];
    }

    /**
     * Return the amount of rows.
     */
    public int getRowCount() {
        return content == null ? 0 : content.length;
    }

    /**
     * Return the amount of columns.
     */
    public int getColCount() {
        return content == null ? 0 : content[0].length;
    }
}
