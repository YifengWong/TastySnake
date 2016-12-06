package com.example.stevennl.tastysnake.controller.game.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.stevennl.tastysnake.model.Direction;
import com.example.stevennl.tastysnake.model.Map;
import com.example.stevennl.tastysnake.model.Packet;
import com.example.stevennl.tastysnake.model.Pos;
import com.example.stevennl.tastysnake.model.Snake;
import com.example.stevennl.tastysnake.util.bluetooth.BluetoothManager;

/**
 * Thread to send data and receive data during the game.
 * Author: LCY
 */
public class DataTransferThread extends HandlerThread {
    private static final String TAG = "DataTransferThread";
    private Snake snake;
    private SafeHandler handler;

    /**
     * Initialize.
     *
     * @param snake The opponent's snake
     */
    public DataTransferThread(Snake snake) {
        super(TAG);
        this.snake = snake;
    }

    /**
     * Initialize handler.
     */
    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new SafeHandler(snake, getLooper());
    }

    /**
     * Send a packet to remote device.
     *
     * @param pkt The packet to send
     */
    public void send(Packet pkt) {
        handler.obtainMessage(SafeHandler.MSG_SEND, pkt).sendToTarget();
    }

    /**
     * Handle received data.
     *
     * @param pkt The received packet
     */
    public void recv(Packet pkt) {
        handler.obtainMessage(SafeHandler.MSG_RECV, pkt).sendToTarget();
    }

    /**
     * A safe handler that circumvents memory leaks.
     */
    private static class SafeHandler extends Handler {
        private static final int MSG_SEND = 1;
        private static final int MSG_RECV = 2;
        private BluetoothManager manager;
        private Snake snake;

        // Debug fields
        private int recvCnt = 0;
        private int sendCnt = 0;

        /**
         * Initialize.
         *
         * @param looper The looper
         * @param snake The opponent's snake
         */
        private SafeHandler(Snake snake, Looper looper) {
            super(looper);
            this.snake = snake;
            manager = BluetoothManager.getInstance();
        }

        @Override
        public void handleMessage(Message msg) {
            Packet pkt = null;
            switch (msg.what) {
                case MSG_SEND:
                    pkt = (Packet)msg.obj;
                    Log.d(TAG, "Send packet: " + pkt.toString() + " Cnt: " + (++sendCnt));
                    manager.sendToRemote(pkt.toBytes());
                    break;
                case MSG_RECV:
                    pkt = (Packet)msg.obj;
                    Log.d(TAG, "Receive packet: " + pkt.toString() + " Cnt: " + (++recvCnt));
                    handleRecvPkt(pkt);
                    break;
                default:
                    break;
            }
        }

        /**
         * Handle a received packet.
         */
        private void handleRecvPkt(Packet pkt) {
            switch (pkt.getType()) {
                case FOOD_LENGTHEN: {
                    Pos food = pkt.getFood();
                    snake.getMap().createFood(food.getX(), food.getY(), true);
                    break;
                }
                case FOOD_SHORTEN: {
                    Pos food = pkt.getFood();
                    snake.getMap().createFood(food.getX(), food.getY(), false);
                    break;
                }
                case DIRECTION: {
                    Direction direc= pkt.getDirec();
                    snake.move(direc);
                    break;
                }
                default:
                    break;
            }
        }
    }
}
