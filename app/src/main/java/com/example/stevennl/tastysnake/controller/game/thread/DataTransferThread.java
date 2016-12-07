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
 * Thread to send data and receive data during the game.
 * Author: LCY
 */
public class DataTransferThread extends HandlerThread {
    private static final String TAG = "DataTransferThread";
    private SafeHandler handler;
    private OnPacketReceiveListener pktRecvListener;

    /**
     * Call when a packet has been received.
     */
    public interface OnPacketReceiveListener {
        void onPacketReceive(Packet pkt);
    }

    /**
     * Initialize.
     *
     * @param pktRecvListener an {@link OnPacketReceiveListener}
     */
    public DataTransferThread(OnPacketReceiveListener pktRecvListener) {
        super(TAG);
        this.pktRecvListener = pktRecvListener;
    }

    /**
     * Initialize handler.
     */
    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new SafeHandler(pktRecvListener, getLooper());
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
        private WeakReference<BluetoothManager> manager;
        private WeakReference<OnPacketReceiveListener> pktRecvListener_;

        // Debug fields
        private int recvCnt = 0;
        private int sendCnt = 0;

        /**
         * Initialize.
         */
        private SafeHandler(OnPacketReceiveListener pktRecvListener_, Looper looper) {
            super(looper);
            this.pktRecvListener_ = new WeakReference<>(pktRecvListener_);
            manager = new WeakReference<>(BluetoothManager.getInstance());
        }

        @Override
        public void handleMessage(Message msg) {
            Packet pkt = null;
            switch (msg.what) {
                case MSG_SEND:
                    pkt = (Packet)msg.obj;
                    Log.d(TAG, "Send packet: " + pkt.toString() + " Cnt: " + (++sendCnt));
                    manager.get().sendToRemote(pkt.toBytes());
                    break;
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
