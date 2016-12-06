package com.example.stevennl.tastysnake.controller.game.thread;

import android.util.Log;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.model.Map;
import com.example.stevennl.tastysnake.model.Packet;
import com.example.stevennl.tastysnake.model.Pos;
import com.example.stevennl.tastysnake.util.CommonUtil;

/**
 * Thread to create food during the game.
 * Author: LCY
 */
public class FoodThread extends Thread {
    private static final String TAG = "FoodThread";
    private DataTransferThread dataThread;
    private Map map;

    /**
     * Initialize.
     *
     * @param map The map to create food on
     * @param dataThread The thread to send data
     */
    public FoodThread(Map map, DataTransferThread dataThread) {
        super(TAG);
        this.map = map;
        this.dataThread = dataThread;
    }

    @Override
    public void run() {
        while (true) {
            try {
                boolean lengthen = (CommonUtil.randInt(2) == 0);
                Pos food = map.createFood(lengthen);
                dataThread.send(Packet.food(food.getX(), food.getY(), lengthen));
                Thread.sleep(Config.INTERVAL_FOOD);
            } catch (InterruptedException e) {
                Log.e(TAG, "Error: ", e);
                break;
            }
        }
    }
}
