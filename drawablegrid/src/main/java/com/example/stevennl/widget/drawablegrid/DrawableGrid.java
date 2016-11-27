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

    private Paint paint = new Paint();

    private DrawableGridInfo[][] infos;

    /**
     * Initialize from code.
     *
     * @param context the context
     */
    public DrawableGrid(Context context) {
        this(context, null);
    }

    /**
     * Initialize from XML resources file.
     *
     * @param context the context
     * @param attrs the attributes set
     */
    public DrawableGrid(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "Constructor called.");
        if (attrs != null) {
            initCustomAttrs(context, attrs);
        }
        initInfos();
    }

    /**
     * Draw view contents.
     *
     * @param canvas the canvas on which the background will be drawn
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
     * @param context the context
     * @param attrs the attributes set
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
     * @param canvas the canvas on which the background will be drawn
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
     * Draw grid info.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    private void drawGridInfo(Canvas canvas) {
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                paint.setColor(infos[i][j].color);
                float left = horOffset + 1 + j * (horInterval + 1);
                float top = verOffset + 1 + i * (verInterval + 1);
                float right = left + horInterval - 1;
                float bottom = top + verInterval - 1;
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }

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
}
