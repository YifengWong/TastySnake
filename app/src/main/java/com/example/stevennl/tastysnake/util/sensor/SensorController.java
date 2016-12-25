package com.example.stevennl.tastysnake.util.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.model.Direction;

/**
 * A class help to control accelerometer. Implemented as a singleton.
 * Direction Sample:
 *
 *      DOWN    UP      LEFT      RIGHT
 * X    6       -6      0       0
 * Y    0       0       -5      7
 * Z    7       7       8       6
 *
 * Author: Yifeng Wong
 */
public class SensorController {
    private static final String TAG = "SensorController";

    private static float HIGH_SEN = 4.1f;//之字形
    private static float MIN_SEN = 1.0f;//抖动
    private static float ACC_BOUND = 0.11f;

    private static SensorController instance;

    private SensorManager manager;
    private SensorEventListener eventListener;

    private float xAccVal = 0; // acceleration in x from sensor
    private float yAccVal = 0; // acceleration in y from sensor

    private float accXAcc = 0; // acceleration to show the speed of user shaking in x
    private float accYAcc = 0; // acceleration to show the speed of user shaking in x

    /**
     * Return the only instance.
     *
     * @param context The context
     */
    public static SensorController getInstance(Context context) {
        if (instance == null) {
            instance = new SensorController(context);
        }
        return instance;
    }

    /**
     * Initialize.
     *
     * @param context The context
     */
    private SensorController(Context context) {
        this.manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        this.eventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        if (event.values[0] > 0 && event.values[0] > xAccVal ||
                                event.values[0] < 0 && event.values[0] < xAccVal) {
                            accXAcc = event.values[0] - xAccVal;
                        }

                        if (event.values[1] > 0 && event.values[1] > yAccVal ||
                                event.values[1] < 0 && event.values[1] < yAccVal) {
                            accYAcc = event.values[1] - yAccVal;
                        }

                        xAccVal = event.values[0];
                        yAccVal = event.values[1];
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Do nothing
            }
        };
    }

    public Direction getLRDirection() {
        if (accYAcc - ACC_BOUND > 0 && yAccVal > MIN_SEN || yAccVal > HIGH_SEN) {
            return Direction.RIGHT;
        } else if (accYAcc + ACC_BOUND < 0 && yAccVal < -MIN_SEN || yAccVal < -HIGH_SEN) {
            return Direction.LEFT;
        }
        return Direction.NONE;
    }

    public Direction getUDDirection() {
        if (accXAcc - ACC_BOUND > 0 && accXAcc > MIN_SEN || xAccVal > HIGH_SEN) {
            return Direction.DOWN;
        } else if (accXAcc + ACC_BOUND < 0 && accXAcc < -MIN_SEN || xAccVal < -HIGH_SEN) {
            return Direction.UP;
        }
        return Direction.NONE;
    }

    public float getxAccVal() {
        return xAccVal;
    }
    public float getyAccVal() {
        return yAccVal;
    }

    public float getAccXAcc() {
        return accXAcc;
    }
    public float getAccYAcc() {
        return accYAcc;
    }

    /**
     * Return current direction of inclination.
     */
    public Direction getDirection() {
        if (Math.abs(yAccVal) < Math.abs(xAccVal)) {
            if ((xAccVal - Config.GRAVITY_SENSITIVITY) > yAccVal) {
                return Direction.DOWN;
            } else if ((xAccVal + Config.GRAVITY_SENSITIVITY) < yAccVal) {
                return Direction.UP;
            }
        } else {
            if ((yAccVal + Config.GRAVITY_SENSITIVITY) < xAccVal) {
                return Direction.LEFT;
            } else if ((yAccVal - Config.GRAVITY_SENSITIVITY) > xAccVal) {
                return Direction.RIGHT;
            }
        }
        return Direction.NONE;
    }

    /**
     * Register sensor event listener.
     */
    public void register() {
        manager.registerListener(eventListener, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Unregister sensor event listener.
     */
    public void unregister() {
        manager.unregisterListener(eventListener);
    }
}
