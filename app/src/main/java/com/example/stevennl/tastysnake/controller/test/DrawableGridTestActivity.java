package com.example.stevennl.tastysnake.controller.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.base.BaseActivity;
import com.example.stevennl.tastysnake.model.Direction;
import com.example.stevennl.tastysnake.model.Map;
import com.example.stevennl.tastysnake.model.Snake;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.sensor.SensorController;
import com.example.stevennl.tastysnake.widget.DrawableGrid;

import java.util.Timer;
import java.util.TimerTask;

public class DrawableGridTestActivity extends BaseActivity {
    private static final String TAG = "GridTestActivity";

    private Snake mySnake;
    private Snake enemySnake;
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
        mySnake = new Snake(Snake.Type.SERVER, map, Config.COLOR_SNAKE_MY);
        enemySnake = new Snake(Snake.Type.CLIENT, map, Config.COLOR_SNAKE_ENEMY);
        DrawableGrid grid = (DrawableGrid) findViewById(R.id.drawablegrid_test_grid);
        grid.setMap(map);
        grid.setBgColor(Config.COLOR_MAP_BG);
        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.showToast(DrawableGridTestActivity.this, "Click");
            }
        });
//        for (int i = 0; i < Point.Type.values().length; ++i) {
//            map.getPoint(i, i).setColor(Config.COLOR_SNAKE_MY);
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
                if (mySnake != null && enemySnake != null) {
                    Direction dir = Direction.NONE;
                    Direction snakeDirec = mySnake.getDirec();
                    if (snakeDirec == Direction.DOWN || snakeDirec == Direction.UP) {
                        dir = sensorCtrl.getLRDirection();
                    } else if (snakeDirec == Direction.LEFT || snakeDirec == Direction.RIGHT) {
                        dir = sensorCtrl.getUDDirection();
                    }
                    Snake.MoveResult res = mySnake.move(dir);
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
                }
            }
        }, 0, Config.FREQUENCY_MOVE);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                map.createFood(lengthen = !lengthen);
            }
        }, 0, Config.FREQUENCY_FOOD);
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
