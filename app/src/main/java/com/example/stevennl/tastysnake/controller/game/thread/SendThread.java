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

import java.lang.ref.WeakReference;

/**
 * Thread to send data during the game.
 * Author: LCY
 */
public class SendThread extends HandlerThread {
    private static final String TAG = "SendThread";
    private SafeHandler handler;

    public SendThread() {
        super(TAG);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new SafeHandler(getLooper());
    }

    public void send(Packet pkt) {
        handler.obtainMessage(SafeHandler.MSG_SEND, pkt).sendToTarget();
    }

    private static class SafeHandler extends Handler {
        private static final int MSG_SEND = 1;
        private WeakReference<BluetoothManager> manager;

        // Debug fields
        private int sendCnt = 0;

        private SafeHandler(Looper looper) {
            super(looper);
            manager = new WeakReference<>(BluetoothManager.getInstance());
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SEND:
                    Packet pkt = (Packet)msg.obj;
                    Log.d(TAG, "Send packet: " + pkt.toString() + " Cnt: " + (++sendCnt));
                    manager.get().sendToRemote(pkt.toBytes());
                    break;
                default:
                    break;
            }
        }
    }
}
