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

    private Direction direction = Direction.NONE;
    private float xValue = 0;
    private float yValue = 0;
    private float zValue = 0;

    private SensorManager sManager;
    private static Context context;
    private static SensorController instance;

    // Needed: set context at first
    public static SensorController getInstance() {
        if (instance == null) {
            instance = new SensorController();
        }
        return instance;
    }

    public static void setContext(Context _context) {
        if (context != null) return;
        context = _context;
    }

    private SensorController() {
        super();
        sManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerListener = getListener();
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
        direction = Direction.NONE;
        if (Math.abs(yValue) < Math.abs(xValue)) {
            // left and right
            if ((xValue - Constants.GRAVITY_SENSITIVITY) > yValue) {
                direction = Direction.LEFT;
            } else if ((xValue + Constants.GRAVITY_SENSITIVITY) < yValue) {
                direction = Direction.RIGHT;
            }
        } else {
            // up and down
            if ((yValue + Constants.GRAVITY_SENSITIVITY) < xValue) {
                direction = Direction.UP;
            } else if ((yValue - Constants.GRAVITY_SENSITIVITY) > xValue) {
                direction = Direction.DOWN;
            }
        }
        return direction;
    }

}
