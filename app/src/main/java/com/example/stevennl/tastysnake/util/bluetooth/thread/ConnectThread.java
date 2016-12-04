package com.example.stevennl.tastysnake.util.bluetooth.thread;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnErrorListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnSocketEstablishedListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnStateChangedListener;

import java.io.IOException;
import java.util.UUID;

/**
 * Bluetooth client thread.
 * Author: LCY
 */
public class ConnectThread extends Thread {
    private static final String TAG = "ConnectThread";
    private final BluetoothSocket socket;
    private final OnSocketEstablishedListener onSocketEstablishedListener;
    private final OnStateChangedListener stateListener;
    private final OnErrorListener errorListener;

    /**
     * Initialize the thread.
     *
     * @param device The device to connect
     */
    public ConnectThread(BluetoothDevice device,
                         OnSocketEstablishedListener onSocketEstablishedListener,
                         OnStateChangedListener stateListener,
                         OnErrorListener errorListener) {
        this.onSocketEstablishedListener = onSocketEstablishedListener;
        this.stateListener = stateListener;
        this.errorListener = errorListener;
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(
                    UUID.fromString(Config.BLUETOOTH_UUID_STR));
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            errorListener.onError(OnErrorListener.ERR_SOCKET_CREATE, e);
        }
        socket = tmp;
    }

    /**
     * Connect the device and get the socket.
     */
    @Override
    public void run() {
        if (socket == null) {
            return;
        }
        try {
            Log.d(TAG, "Connecting remote device...");
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            socket.connect();
        } catch (IOException connectException) {
            Log.e(TAG, "Error:", connectException);
            errorListener.onError(OnErrorListener.ERR_CLIENT_SOCKET_CONNECT, connectException);
            try {
                socket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Error:", closeException);
                errorListener.onError(OnErrorListener.ERR_SOCKET_CLOSE, closeException);
            }
            return;
        }
        onSocketEstablishedListener.onSocketEstablished(socket, stateListener, errorListener);
        Log.d(TAG, "Thread ended.");
    }

    /**
     * Cancel an in-progress connection and close the socket.
     */
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            errorListener.onError(OnErrorListener.ERR_SOCKET_CLOSE, e);
        }
    }
}
