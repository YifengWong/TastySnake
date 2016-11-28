package com.example.stevennl.tastysnake.util.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

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
    public static final int DIR_NULL = 0;
    public static final int DIR_LEFT = 1;
    public static final int DIR_RIGHT = 2;
    public static final int DIR_UP = 3;
    public static final int DIR_DOWN = 4;

    private SensorEventListener accelerometerListener;

    private int sensorType = Sensor.TYPE_ACCELEROMETER;

    private int direction = DIR_NULL;
    private float xValue = 0;
    private float yValue = 0;
    private float zValue = 0;


    private SensorManager sManager;
    private Context context;

    public SensorController(Context context) {
        super();
        this.context = context;
        sManager = (SensorManager)this.context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerListener = getListener();
    }

    private SensorEventListener getListener() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                xValue = event.values[0];
                yValue = event.values[1];
                zValue = event.values[2];

                if (Math.abs(yValue) < Math.abs(xValue)) {
                    // left and right
                    if (xValue > yValue) {
                        direction = DIR_LEFT;
                    } else {
                        direction = DIR_RIGHT;
                    }
                } else {
                    // up and down
                    if (yValue < xValue) {
                        direction = DIR_UP;
                    } else {
                        direction = DIR_DOWN;
                    }
                }
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

    public int getDirection() {
        return direction;
    }

}
