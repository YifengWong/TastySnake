package com.example.stevennl.tastysnake.ui.test;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.widget.drawablegrid.DrawableGrid;
import com.example.stevennl.widget.drawablegrid.DrawableGridInfo;

import java.util.Random;

public class DrawableGridTestActivity extends AppCompatActivity {
    private static final String TAG = "GridTestActivity";
    private int x, y;
    private boolean flag = true;
    private Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_grid_test);
        initDrawableGrid();
    }

    private void initDrawableGrid() {
        final DrawableGrid grid = (DrawableGrid) findViewById(R.id.drawablegrid_test_grid);
        final DrawableGridInfo[][] infos = grid.getInfos();
        final int col = grid.getColCount(), row = grid.getRowCount();
        final Random rand = new Random();
        final Handler handler = new Handler();
        x = y = 0;
        r = new Runnable() {
            @Override
            public void run() {
                int c = Color.rgb(rand.nextInt() * 255, rand.nextInt() * 255, rand.nextInt() * 255);
                if (flag) {
                    infos[x++][y].color = c;
                } else {
                    infos[x][y++].color = c;
                }
                flag = !flag;
                grid.invalidate();
                if (!(x == row || y == col)) {
                    handler.postDelayed(r, 50);
                }
            }
        };
        handler.postDelayed(r, 1000);
    }
}
