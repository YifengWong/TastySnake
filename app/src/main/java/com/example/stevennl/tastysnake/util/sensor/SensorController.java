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
 *      Left    Right   Up      Down
 * X    6       -6      0       0
 * Y    0       0       -5      7
 * Z    7       7       8       6
 *
 * Author: Yifeng Wong
 */
public class SensorController {
    private static final String TAG = "SensorController";
    private static SensorController instance;

    private SensorManager manager;
    private SensorEventListener eventListener;

    private float xAccVal = 0;
    private float yAccVal = 0;

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
