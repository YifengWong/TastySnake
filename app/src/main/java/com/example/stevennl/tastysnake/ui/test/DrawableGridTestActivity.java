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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_grid_test);
        initDrawableGrid();
    }

    private void initDrawableGrid() {
        DrawableGrid grid = (DrawableGrid) findViewById(R.id.drawablegrid_test_grid);
        DrawableGridInfo[][] infos = grid.getInfos();
        for (int i = 0; i < DrawableGridInfo.Type.values().length; ++i) {
            infos[i][i].color = Color.rgb(204, 0, 0);
            infos[i][i].type = DrawableGridInfo.Type.values()[i];
        }
        grid.invalidate();
    }
}
