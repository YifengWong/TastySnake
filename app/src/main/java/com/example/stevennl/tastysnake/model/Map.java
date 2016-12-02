package com.example.stevennl.tastysnake.model;

import android.graphics.Color;

/**
 Game map.
 */
public class Map {
    private Point[][] content;
    //private Pos food;
    private boolean weather; // False for stretch True for shorten

    public boolean isWeather() {
        return weather;
    }

    public void setWeather(boolean weather) {
        this.weather = weather;
    }

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
    static final int foodColor = Color.rgb(0, 204, 0);
    public boolean createFood(int x, int y, int type) {
        switch (type) {
            case 0:
                if (content[x][y].getType() == Point.Type.BLANK)
                    content[x][y] = new Point(foodColor, Point.Type.FOOD);
                else
                    return false;
                break;
        }
        return true;
    }
    public void setPoint(Pos p, Point c) {
        content[p.getX()][p.getY()] = c;
    }
    /**
     * Return a point on the map.
     *
     * @param p the position of the point
     */
    public Point getPoint(Pos p) {
        return content[p.getX()][p.getY()];
    }

    /**
     * Return a point on the map.
     *
     * @param x the row number of the point
     * @param y the column number of the point
     */
    public Point getPoint(int x, int y) {
        return content[x][y];
    }

    /**
     * Return the map content.
     */
    public Point[][] getContent() {
        return content;
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
