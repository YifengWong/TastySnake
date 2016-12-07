package com.example.stevennl.tastysnake.controller.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.model.Direction;
import com.example.stevennl.tastysnake.model.Map;
import com.example.stevennl.tastysnake.model.Snake;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.sensor.SensorController;
import com.example.stevennl.tastysnake.widget.DrawableGrid;

import java.util.Timer;
import java.util.TimerTask;

public class DrawableGridTestActivity extends AppCompatActivity {
    private static final String TAG = "GridTestActivity";

    private Snake snakeServer;
    private Snake snakeClient;
    private Map map;

    private Timer timer;
    private SensorController sensorCtrl;

    private boolean lengthen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable_grid_test);
        sensorCtrl = SensorController.getInstance(this);
        map = Map.gameMap();
        snakeServer = new Snake(Snake.Type.SERVER, map);
        snakeClient = new Snake(Snake.Type.CLIENT, map);
        DrawableGrid grid = (DrawableGrid) findViewById(R.id.drawablegrid_test_grid);
        grid.setMap(map);
        grid.setBgColor(Config.COLOR_MAP_BG);
//        for (int i = 0; i < Point.Type.values().length; ++i) {
//            map.getPoint(i, i).setColor(Color.rgb(204, 0, 0));
//            map.getPoint(i, i).setType(Point.Type.values()[i]);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorCtrl.register();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorCtrl.unregister();
        stopTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {  // Gravity sensor and snake move thread
            @Override
            public void run() {
                if (snakeServer != null && snakeClient != null) {
                    Direction dir = sensorCtrl.getDirection();
                    Snake.MoveResult res = snakeServer.move(dir);
                    switch (res) {
                        case SUC:
                            break;
                        case SUICIDE:
                        case HIT_ENEMY:
                        case OUT:
                            showToast("Snake " + res.name());
                            stopTimer();
                            break;
                        default:
                            break;
                    }
                    Log.d(TAG, "run: " + dir);
                    map.createFood(lengthen = !lengthen);
                }
            }
        }, 0, Config.INTERVAL_MOVE);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void showToast(final String msg) {
        DrawableGridTestActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommonUtil.showToast(DrawableGridTestActivity.this, msg);
            }
        });
    }
}
