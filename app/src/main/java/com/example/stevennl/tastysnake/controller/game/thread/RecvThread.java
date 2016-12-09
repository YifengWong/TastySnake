package com.example.stevennl.tastysnake.controller.game.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.stevennl.tastysnake.model.Packet;
import com.example.stevennl.tastysnake.util.bluetooth.BluetoothManager;

import java.lang.ref.WeakReference;

/**
 * Thread to receive data during the game.
 * Author: LCY
 */
public class RecvThread extends HandlerThread {
    private static final String TAG = "RecvThread";
    private RecvThread.SafeHandler handler;
    private RecvThread.OnPacketReceiveListener pktRecvListener;

    /**
     * Called when a packet is received.
     */
    public interface OnPacketReceiveListener {
        void onPacketReceive(Packet pkt);
    }

    public RecvThread(RecvThread.OnPacketReceiveListener pktRecvListener) {
        super(TAG);
        this.pktRecvListener = pktRecvListener;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new RecvThread.SafeHandler(pktRecvListener, getLooper());
    }

    public void recv(Packet pkt) {
        handler.obtainMessage(RecvThread.SafeHandler.MSG_RECV, pkt).sendToTarget();
    }

    private static class SafeHandler extends Handler {
        private static final int MSG_RECV = 1;
        private WeakReference<RecvThread.OnPacketReceiveListener> pktRecvListener_;

        // Debug fields
        private int recvCnt = 0;

        private SafeHandler(RecvThread.OnPacketReceiveListener pktRecvListener_, Looper looper) {
            super(looper);
            this.pktRecvListener_ = new WeakReference<>(pktRecvListener_);
        }

        @Override
        public void handleMessage(Message msg) {
            Packet pkt = null;
            switch (msg.what) {
                case MSG_RECV:
                    pkt = (Packet)msg.obj;
                    Log.d(TAG, "Receive packet: " + pkt.toString() + " Cnt: " + (++recvCnt));
                    pktRecvListener_.get().onPacketReceive(pkt);
                    break;
                default:
                    break;
            }
        }
    }
}
