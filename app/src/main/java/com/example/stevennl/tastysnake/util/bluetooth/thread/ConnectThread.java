package com.example.stevennl.tastysnake.util.bluetooth.thread;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.stevennl.tastysnake.Constants;
import com.example.stevennl.tastysnake.util.bluetooth.listener.SocketListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.ConnectListener;

import java.io.IOException;
import java.util.UUID;

/**
 * Bluetooth client thread.
 * Author: LCY
 */
public class ConnectThread extends Thread {
    private static final String TAG = "ConnectThread";
    private final BluetoothSocket socket;
    private final SocketListener socketListener;
    private final ConnectListener connListener;

    /**
     * Initialize the thread.
     *
     * @param device The device to connect
     * @param socketListener A socket listener
     * @param connListener A connection listener
     */
    public ConnectThread(BluetoothDevice device,
                         SocketListener socketListener,
                         ConnectListener connListener) {
        this.socketListener = socketListener;
        this.connListener = connListener;
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(
                    UUID.fromString(Constants.BLUETOOTH_UUID_STR));
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            connListener.onError(ConnectListener.ERR_SOCKET_CREATE, e);
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
            connListener.onError(ConnectListener.ERR_CLIENT_SOCKET_CONNECT, connectException);
            try {
                socket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Error:", closeException);
                connListener.onError(ConnectListener.ERR_SOCKET_CLOSE, closeException);
            }
            return;
        }
        connListener.onClientSocketEstablished();
        socketListener.onSocketEstablished(socket);
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
            connListener.onError(ConnectListener.ERR_SOCKET_CLOSE, e);
        }
    }
}
