package com.example.stevennl.tastysnake.controller.game.thread;

import android.content.Context;
import android.util.Log;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.model.Direction;
import com.example.stevennl.tastysnake.model.Packet;
import com.example.stevennl.tastysnake.model.Snake;
import com.example.stevennl.tastysnake.util.sensor.SensorController;

/**
 * Thread to move the snake during the game.
 * Author: LCY
 */
public class MoveThread extends Thread {
    private static final String TAG = "MoveThread";
    private SensorController sensorCtrl;
    private DataTransferThread dataThread;
    private Snake snake;

    /**
     * Initialize.
     *
     * @param context The context
     * @param snake The snake to move
     * @param dataThread The thread to send data
     */
    public MoveThread(Context context, Snake snake, DataTransferThread dataThread) {
        super(TAG);
        this.snake = snake;
        this.dataThread = dataThread;
        sensorCtrl = SensorController.getInstance(context);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Direction direc = sensorCtrl.getDirection();
                dataThread.send(Packet.direction(direc));
                if (snake.move(direc) != Snake.MoveResult.SUC) {
                    interrupt();
                }
                Thread.sleep(Config.INTERVAL_MOVE);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error: ", e);
                break;
            }
        }
    }
}
