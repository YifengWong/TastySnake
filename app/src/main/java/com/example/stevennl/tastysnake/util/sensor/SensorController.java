package com.example.stevennl.tastysnake.util.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.stevennl.tastysnake.Constants;
import com.example.stevennl.tastysnake.model.Direction;

/**
 * A class help to control accelerometer.
 * Direction Sample:
 *
 *      Left    Right   Up      Down
 * X    6       -6      0       0
 * Y    0       0       -5      7
 * Z    7       7       8       6
 *
 * Author: CrazeWong
 */
public class SensorController {
    private SensorEventListener accelerometerListener;

    private int sensorType = Sensor.TYPE_ACCELEROMETER;
    private float xValue = 0;
    private float yValue = 0;
    private float zValue = 0;

    private SensorManager sManager;
    private static SensorController instance;

    public static SensorController getInstance(Context context) {
        if (instance == null) {
            instance = new SensorController(context);
        }
        return instance;
    }

    private SensorController(Context context) {
        super();
        this.sManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometerListener = getListener();
    }

    private SensorEventListener getListener() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                xValue = event.values[0];
                yValue = event.values[1];
                zValue = event.values[2];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Nothing
            }
        };
    }

    public void registerSensor() {
        sManager.registerListener(accelerometerListener,
                sManager.getDefaultSensor(sensorType),SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterSensor() {
        sManager.unregisterListener(accelerometerListener);
    }

    public float getxValue() {
        return this.xValue;
    }
    public float getyValue() {
        return this.yValue;
    }
    public float getzValue() {
        return this.zValue;
    }

    public Direction getDirection() {
        if (Math.abs(yValue) < Math.abs(xValue)) {
            return getLRDirection();
        } else {
            return getUDDirection();
        }
    }
    // 由于其实某一时刻只需要两个方向(如向左移动时，只有上下方向是有效的)，这两个方法供上层使用可以提高灵敏度。
    public Direction getLRDirection() {
        if ((xValue - Constants.GRAVITY_SENSITIVITY) > yValue) {
            return Direction.LEFT;
        } else if ((xValue + Constants.GRAVITY_SENSITIVITY) < yValue) {
            return Direction.RIGHT;
        }
        return Direction.NONE;
    }
    public Direction getUDDirection() {
        if ((yValue + Constants.GRAVITY_SENSITIVITY) < xValue) {
            return Direction.UP;
        } else if ((yValue - Constants.GRAVITY_SENSITIVITY) > xValue) {
            return Direction.DOWN;
        }
        return Direction.NONE;
    }

}
