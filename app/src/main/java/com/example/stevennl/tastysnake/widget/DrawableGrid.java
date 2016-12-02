package com.example.stevennl.tastysnake.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.stevennl.tastysnake.Constants;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.model.Map;
import com.example.stevennl.tastysnake.model.Point;

/**
 * This view can divide the screen to several grids and draw the content of each grid.
 * Author: LCY
 */
public class DrawableGrid extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "DrawableGrid";

    private int rowCount = 1;
    private int colCount = 1;
    private int bgColor;

    private int width = 0;
    private int height = 0;
    private int horInterval = 0;
    private int verInterval = 0;
    private int horLineCnt = 0;
    private int verLineCnt = 0;
    private int horOffset = 0;
    private int verOffset = 0;

    private Map map;
    private Paint paint;
    private boolean drawing;

    /**
     * Set the map to be drawn.
     */
    public void setMap(Map map) {
        Log.d(TAG, "setMap() called.");
        this.map = map;
        this.rowCount = map.getRowCount();
        this.colCount = map.getColCount();
    }

    /**
     * Set the background color.
     */
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
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
        initSurfaceView();
        initPaint();
    }

    /**
     * Initialize the surface view conf.
     */
    private void initSurfaceView() {
        setKeepScreenOn(true);
        getHolder().addCallback(this);
    }

    /**
     * Initialize the paint.
     */
    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged() called.");
    }

    /**
     * This is called immediately before a surface is being destroyed.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed() called.");
        drawing = false;
    }

    /**
     * This is called immediately after the surface is first created.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated() called.");
        drawing = true;
        DrawThread drawThread = new DrawThread(holder);
        drawThread.start();
    }

    /**
     * Thread to draw the map content.
     */
    private class DrawThread extends Thread {
        private SurfaceHolder holder_;
        private Canvas canvas;

        private DrawThread(SurfaceHolder holder_) {
            this.holder_ = holder_;
        }

        @Override
        public void run() {
            computeSize();
            while (drawing) {
                try {
                    draw();
                    Thread.sleep(Constants.INTERVAL_DRAW);
                } catch (Exception e) {
                    Log.e(TAG, "Error: ", e);
                }
            }
        }

        private void draw() {
            try {
                canvas = holder_.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(bgColor);
                    drawMapContent(canvas);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error: ", e);
            } finally {
                if (canvas != null) {
                    holder_.unlockCanvasAndPost(canvas);
                }
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
     * Draw the content of each grid.
     *
     * @param canvas The canvas on which the background will be drawn
     */
    private void drawMapContent(Canvas canvas) {
        if (map == null) return;
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                paint.setColor(map.getPoint(i, j).getColor());
                float left = horOffset + 1 + j * (horInterval + 1);
                float top = verOffset + 1 + i * (verInterval + 1);
                float right = left + horInterval - 1;
                float bottom = top + verInterval - 1;
                drawGrid(left, top, right, bottom, map.getPoint(i, j).getType(), canvas);
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
                          Point.Type type, Canvas canvas) {
        final float
                gridWidth = right - left,
                gridHeight = bottom - top,
                offsetHor = (right - left) / 4,
                offsetVer = (bottom - top) / 4;
        switch (type) {
            case BLANK:
                canvas.drawRect(left, top, right, bottom, paint);
                break;
            case FOOD_LENGTHEN:
            case FOOD_SHORTEN:
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
