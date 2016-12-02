package com.example.stevennl.tastysnake.ui.test;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.stevennl.tastysnake.Constants;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.model.Direction;
import com.example.stevennl.tastysnake.model.Map;
import com.example.stevennl.tastysnake.model.Pos;
import com.example.stevennl.tastysnake.model.Point;
import com.example.stevennl.tastysnake.model.Snake;
import com.example.stevennl.tastysnake.util.sensor.SensorController;
import com.example.stevennl.tastysnake.widget.DrawableGrid;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DrawableGridTestActivity extends AppCompatActivity {
    private static final String TAG = "GridTestActivity";

    private Snake snake;
    private Map map;

    private Timer timer;
    private SensorController sensorCtrl;

    private boolean lengthen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_grid_test);
        sensorCtrl = new SensorController(this);
        map = new Map(Constants.MAP_ROW, Constants.MAP_COL);
        snake = new Snake(0, map);
        DrawableGrid grid = (DrawableGrid) findViewById(R.id.drawablegrid_test_grid);
        grid.setMap(map);
//        for (int i = 0; i < Point.Type.values().length; ++i) {
//            map.getPoint(i, i).setColor(Color.rgb(204, 0, 0));
//            map.getPoint(i, i).setType(Point.Type.values()[i]);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorCtrl.registerSensor();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorCtrl.unregisterSensor();
        stopTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {  // Gravity sensor and snake move thread
            @Override
            public void run() {
                if (snake != null) {
                    Direction dir = sensorCtrl.getDirection();
                    if (!snake.move(dir)) {
                        finish();
                    }
                    Log.d(TAG, "run: " + dir);
                    map.createFood(lengthen = !lengthen);
                }
            }
        }, 0, Constants.INTERVAL_MOVE);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
