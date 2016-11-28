package com.example.stevennl.widget.drawablegrid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * This view can divide the screen to several grids and control the content of each grid.
 * Author: LCY
 */
public class DrawableGrid extends View {
    private static final String TAG = "DrawableGrid";

    private int rowCount = 1;
    private int colCount = 1;
    private boolean showGridLine = true;
    private int gridLineColor = Color.LTGRAY;

    private int width = 0;
    private int height = 0;
    private int horInterval = 0;
    private int verInterval = 0;
    private int horLineCnt = 0;
    private int verLineCnt = 0;
    private int horOffset = 0;
    private int verOffset = 0;

    private Paint paint;

    private DrawableGridInfo[][] infos;

    /**
     * Getters and setters
     */
    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public DrawableGridInfo[][] getInfos() {
        return infos;
    }

    public void setInfos(DrawableGridInfo[][] infos) {
        this.infos = infos;
    }

    /**
     * Initialize from code.
     *
     * @param context The context
     */
    public DrawableGrid(Context context) {
        this(context, null);
    }

    /**
     * Initialize from XML resources file.
     *
     * @param context The context
     * @param attrs The attributes set
     */
    public DrawableGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "Constructor called.");
        if (attrs != null) {
            initCustomAttrs(context, attrs);
        }
        initPaint();
        initInfos();
    }

    /**
     * Draw view contents.
     *
     * @param canvas The canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw() called.");
        computeSize();
        if (showGridLine) {
            drawGridLine(canvas);
        }
        drawGridInfo(canvas);
    }

    /**
     * Initialize fields from custom attributes.
     *
     * @param context The context
     * @param attrs The attributes set
     */
    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.drawable_grid_attr);
        rowCount = arr.getInt(R.styleable.drawable_grid_attr_rowCount, 1);
        colCount = arr.getInt(R.styleable.drawable_grid_attr_colCount, 1);
        gridLineColor = arr.getColor(R.styleable.drawable_grid_attr_gridLineColor, Color.LTGRAY);
        showGridLine = arr.getBoolean(R.styleable.drawable_grid_attr_showGridLine, true);
        arr.recycle();
        Log.d(TAG, "Attrs: " + rowCount + " " + colCount + " " + showGridLine);
    }

    /**
     * Initialize the paint.
     */
    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    /**
     * Initialize infos field.
     */
    private void initInfos() {
        infos = new DrawableGridInfo[rowCount][colCount];
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                infos[i][j] = new DrawableGridInfo();
            }
        }
    }

    /**
     * Compute sizes and drawing locations of the grid.
     */
    private void computeSize() {
        width = getWidth();
        height = getHeight();
        horLineCnt = rowCount + 1;
        verLineCnt = colCount + 1;
        int horRemain = width - verLineCnt;
        int verRemain = height - horLineCnt;
        horInterval = horRemain / colCount;
        verInterval = verRemain / rowCount;
        horOffset = (horRemain % colCount) / 2;
        verOffset = (verRemain % rowCount) / 2;
    }

    /**
     * Draw grid lines.
     *
     * @param canvas The canvas on which the background will be drawn
     */
    private void drawGridLine(Canvas canvas) {
        paint.setColor(gridLineColor);
        paint.setStrokeWidth(0);  // One pixel
        for (int i = 0; i < verLineCnt; ++i) {  // Draw vertical lines
            float x = horOffset + i * (horInterval + 1);
            canvas.drawLine(x, verOffset, x, height - verOffset, paint);
        }
        for (int i = 0; i < horLineCnt; ++i) {  // Draw horizontal lines
            float y = verOffset + i * (verInterval + 1);
            canvas.drawLine(horOffset, y, width - horOffset, y, paint);
        }
    }

    /**
     * Draw the content of each grid.
     *
     * @param canvas The canvas on which the background will be drawn
     */
    private void drawGridInfo(Canvas canvas) {
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                paint.setColor(infos[i][j].color);
                float left = horOffset + 1 + j * (horInterval + 1);
                float top = verOffset + 1 + i * (verInterval + 1);
                float right = left + horInterval - 1;
                float bottom = top + verInterval - 1;
                drawGrid(left, top, right, bottom, infos[i][j].type, canvas);
            }
        }
    }

    /**
     * Draw the content of one grid according to its type.
     *
     * @param left   The left side of the grid
     * @param top    The top side of the grid
     * @param right  The right side of the grid
     * @param bottom The bottom side of the grid
     * @param type   The type of the grid.
     * @param canvas The canvas on which the background will be drawn
     */
    private void drawGrid(float left, float top, float right, float bottom,
                          DrawableGridInfo.Type type, Canvas canvas) {
        final float
                gridWidth = right - left,
                gridHeight = bottom - top,
                offsetHor = (right - left) / 4,
                offsetVer = (bottom - top) / 4;
        switch (type) {
            case BLANK:
                canvas.drawRect(left, top, right, bottom, paint);
                break;
            case FOOD:
                canvas.drawCircle((left + right) / 2, (top + bottom) / 2, (right - left) / 2, paint);
                break;
            case HEAD_L:
                canvas.drawRect(left + gridWidth / 2, top + offsetVer, right, bottom - offsetVer, paint);
                canvas.drawCircle(left + gridWidth / 2, top + gridHeight / 2, (gridHeight - 2 * offsetVer) / 2, paint);
                break;
            case HEAD_U:
                canvas.drawRect(left + offsetHor, top + gridHeight / 2, right - offsetHor, bottom, paint);
                canvas.drawCircle(left + gridWidth / 2, top + gridHeight / 2, (gridWidth - 2 * offsetHor) / 2, paint);
                break;
            case HEAD_R:
                canvas.drawRect(left, top + offsetVer, right - gridWidth / 2, bottom - offsetVer, paint);
                canvas.drawCircle(left + gridWidth / 2, top + gridHeight / 2, (gridHeight - 2 * offsetVer) / 2, paint);
                break;
            case HEAD_D:
                canvas.drawRect(left + offsetHor, top, right - offsetHor, bottom - gridHeight / 2, paint);
                canvas.drawCircle(left + gridWidth / 2, top + gridHeight / 2, (gridWidth - 2 * offsetHor) / 2, paint);
                break;
            case BODY_HOR:
                canvas.drawRect(left, top + offsetVer, right, bottom - offsetVer, paint);
                break;
            case BODY_VER:
                canvas.drawRect(left + offsetHor, top, right - offsetHor, bottom, paint);
                break;
            case BODY_L_U:
                canvas.drawRect(left, top + offsetVer, right - offsetHor, bottom - offsetVer, paint);
                canvas.drawRect(left + offsetHor, top, right - offsetHor, bottom - gridHeight / 2, paint);
                break;
            case BODY_L_D:
                canvas.drawRect(left, top + offsetVer, right - offsetHor, bottom - offsetVer, paint);
                canvas.drawRect(left + offsetHor, top + gridHeight / 2, right - offsetHor, bottom, paint);
                break;
            case BODY_R_U:
                canvas.drawRect(left + offsetHor, top + offsetVer, right, bottom - offsetVer, paint);
                canvas.drawRect(left + offsetHor, top, right - offsetHor, bottom - gridHeight / 2, paint);
                break;
            case BODY_R_D:
                canvas.drawRect(left + offsetHor, top + offsetVer, right, bottom - offsetVer, paint);
                canvas.drawRect(left + offsetHor, top + gridHeight / 2, right - offsetHor, bottom, paint);
                break;
        }
    }
}
