package com.example.stevennl.tastysnake.ui.test;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.model.Direction;
import com.example.stevennl.tastysnake.model.Pair;
import com.example.stevennl.tastysnake.model.Snake;
import com.example.stevennl.widget.drawablegrid.DrawableGrid;
import com.example.stevennl.widget.drawablegrid.DrawableGridInfo;

import java.util.Random;

public class DrawableGridTestActivity extends AppCompatActivity {
    private static final String TAG = "GridTestActivity";
    private Snake snake;
    private DrawableGridInfo[][] infos;
    private DrawableGrid grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_grid_test);
        initDrawableGrid();
        initSnake();
    }

    private void initDrawableGrid() {
        grid = (DrawableGrid) findViewById(R.id.drawablegrid_test_grid);
        infos = grid.getInfos();
        /*
        for (int i = 0; i < DrawableGridInfo.Type.values().length; ++i) {
            infos[i][i].color = Color.rgb(204, 0, 0);
            infos[i][i].type = DrawableGridInfo.Type.values()[i];
        }
        */
        grid.invalidate();
    }

    void drawSnake(DrawableGridInfo[][] infos, Snake snake) {
        for (int i = 0; i < infos.length; i ++)
            for (int j = 0; j < infos[0].length; j ++)
                infos[i][j].color = Color.rgb(255, 255, 255);
        for (int idx = 0; idx < snake.body.size(); idx ++) {
            Pair i = snake.body.get(idx);
            infos[i.getX()][i.getY()].color = Color.rgb(204, 0, 0);
            infos[i.getX()][i.getY()].type = snake.type.get(idx);
        }
        grid.invalidate();
    }

    private void initSnake() {
        snake = new Snake(0, grid.getRowCount(), grid.getColCount());
        drawSnake(infos, snake);
        snake.move(Direction.NONE);
        drawSnake(infos, snake);
        snake.move(Direction.RIGHT);
        drawSnake(infos, snake);
        snake.move(Direction.NONE);
        drawSnake(infos, snake);
        snake.move(Direction.LEFT);
        drawSnake(infos, snake);
    }
}
