package com.example.stevennl.tastysnake.model;

/**
 Game map.
 */
public class Map {
    private Point[][] content;
    private Pos food;

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
