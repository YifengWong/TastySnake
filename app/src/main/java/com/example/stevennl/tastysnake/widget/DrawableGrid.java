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

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.model.Direction;
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
    private int bgColor = Color.TRANSPARENT;
    private boolean showGridLine = false;

    private int horInterval = 0;
    private int verInterval = 0;
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
        if (attrs != null) {
            initCustomAttr(context, attrs);
        }
        initSurfaceView();
        initPaint();
    }

    /**
     * Initialize custom attributes.
     */
    private void initCustomAttr(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.DrawableGridAttr);
        showGridLine = arr.getBoolean(R.styleable.DrawableGridAttr_showGridLine, false);
        arr.recycle();
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
                    Thread.sleep(Config.FREQUENCY_DRAW);
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
        int width = getWidth();
        int height = getHeight();
        if (showGridLine) {
            int horLineCnt = rowCount + 1;
            int verLineCnt = colCount + 1;
            width -= verLineCnt;
            height -= horLineCnt;
        }
        horInterval = width / colCount;
        verInterval = height / rowCount;
        horOffset = (width % colCount) / 2;
        verOffset = (height % rowCount) / 2;
    }

    /**
     * Draw the content of each grid.
     *
     * @param canvas The canvas on which the background will be drawn
     */
    private void drawMapContent(Canvas canvas) {
        if (map == null) return;
        float left, top, right, bottom;
        for (int i = 0; i < rowCount; ++i) {
            for (int j = 0; j < colCount; ++j) {
                paint.setColor(map.getPoint(i, j).getColor());
                if (showGridLine) {
                    left = horOffset + 1 + j * (horInterval + 1);
                    top = verOffset + 1 + i * (verInterval + 1);
                } else {
                    left = horOffset + j * horInterval;
                    top = verOffset + i * verInterval;
                }
                right = left + horInterval;
                bottom = top + verInterval;
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
        final float centerHor = (left + right) / 2;
        final float centerVer = (top + bottom) / 2;
        final float gridWidth = right - left;
        final float gridHeight = bottom - top;
        final float offsetHor = (right - left) / 10;
        final float offsetVer = (bottom - top) / 10;
        switch (type) {
            case BLANK:
                canvas.drawRect(left, top, right, bottom, paint);
                break;
            case FOOD_LENGTHEN:
            case FOOD_SHORTEN:
                canvas.drawCircle(centerHor, centerVer, 0.8f * (right - left) / 2, paint);
                break;
            case HEAD_L:
                canvas.drawRect(centerHor, top + offsetVer, right, bottom - offsetVer, paint);
                canvas.drawCircle(centerHor, centerVer, (gridHeight - 2 * offsetVer) / 2, paint);
                drawEyes(left, top, right, bottom, centerHor, centerVer, offsetHor, offsetVer, type, canvas);
                break;
            case HEAD_U:
                canvas.drawRect(left + offsetHor, centerVer, right - offsetHor, bottom, paint);
                canvas.drawCircle(centerHor, centerVer, (gridWidth - 2 * offsetHor) / 2, paint);
                drawEyes(left, top, right, bottom, centerHor, centerVer, offsetHor, offsetVer, type, canvas);
                break;
            case HEAD_R:
                canvas.drawRect(left, top + offsetVer, centerHor, bottom - offsetVer, paint);
                canvas.drawCircle(centerHor, centerVer, (gridHeight - 2 * offsetVer) / 2, paint);
                drawEyes(left, top, right, bottom, centerHor, centerVer, offsetHor, offsetVer, type, canvas);
                break;
            case HEAD_D:
                canvas.drawRect(left + offsetHor, top, right - offsetHor, centerVer, paint);
                canvas.drawCircle(centerHor, centerVer, (gridWidth - 2 * offsetHor) / 2, paint);
                drawEyes(left, top, right, bottom, centerHor, centerVer, offsetHor, offsetVer, type, canvas);
                break;
            case BODY_HOR:
                canvas.drawRect(left, top + offsetVer, right, bottom - offsetVer, paint);
                break;
            case BODY_VER:
                canvas.drawRect(left + offsetHor, top, right - offsetHor, bottom, paint);
                break;
            case BODY_L_U:
                canvas.drawRect(left, top + offsetVer, right - offsetHor, bottom - offsetVer, paint);
                canvas.drawRect(left + offsetHor, top, right - offsetHor, centerVer, paint);
                break;
            case BODY_L_D:
                canvas.drawRect(left, top + offsetVer, right - offsetHor, bottom - offsetVer, paint);
                canvas.drawRect(left + offsetHor, centerVer, right - offsetHor, bottom, paint);
                break;
            case BODY_R_U:
                canvas.drawRect(left + offsetHor, top + offsetVer, right, bottom - offsetVer, paint);
                canvas.drawRect(left + offsetHor, top, right - offsetHor, centerVer, paint);
                break;
            case BODY_R_D:
                canvas.drawRect(left + offsetHor, top + offsetVer, right, bottom - offsetVer, paint);
                canvas.drawRect(left + offsetHor, centerVer, right - offsetHor, bottom, paint);
                break;
        }
    }

    /**
     * Draw eyes upon snake's head.
     *
     * @param left   The left side of the grid
     * @param top    The top side of the grid
     * @param right  The right side of the grid
     * @param bottom The bottom side of the grid
     * @param centerHor The horizontal center
     * @param centerVer The vertical center
     * @param offsetHor The horizontal offset of snake's body
     * @param offsetVer THe vertical offset of snake's body
     * @param type   The type of the grid.
     * @param canvas The canvas on which the background will be drawn
     */
    private void drawEyes(float left, float top, float right, float bottom, float centerHor,
                          float centerVer, float offsetHor, float offsetVer, Point.Type type,
                          Canvas canvas) {
        float bodyWidth, radius, center1, center2;
        switch (type) {
            case HEAD_U:
            case HEAD_D:
                bodyWidth = right - left - 2 * offsetHor;
                radius = 0.75f * bodyWidth / 4;
                center1 = left + offsetHor + 0.25f * bodyWidth;
                center2 = right - offsetHor - 0.25f * bodyWidth;
                paint.setColor(Color.WHITE);
                canvas.drawCircle(center1, centerVer, radius, paint);
                canvas.drawCircle(center2, centerVer, radius, paint);
                paint.setColor(Color.BLACK);
                canvas.drawCircle(center1, centerVer, radius / 2, paint);
                canvas.drawCircle(center2, centerVer, radius / 2, paint);
                break;
            case HEAD_R:
            case HEAD_L:
                bodyWidth = bottom - top - 2 * offsetVer;
                radius = 0.75f * bodyWidth / 4;
                center1 = top + offsetVer + 0.25f * bodyWidth;
                center2 = bottom - offsetVer - 0.25f * bodyWidth;
                paint.setColor(Color.WHITE);
                canvas.drawCircle(centerHor, center1, radius, paint);
                canvas.drawCircle(centerHor, center2, radius, paint);
                paint.setColor(Color.BLACK);
                canvas.drawCircle(centerHor, center1, radius / 2, paint);
                canvas.drawCircle(centerHor, center2, radius / 2, paint);
                break;
            default:
                break;
        }
    }
}
