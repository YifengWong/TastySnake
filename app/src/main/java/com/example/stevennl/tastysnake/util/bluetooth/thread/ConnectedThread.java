package com.example.stevennl.tastysnake.util.bluetooth.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.stevennl.tastysnake.util.bluetooth.listener.ConnectListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Thread to manage a connected socket.
 * Author: LCY
 */
public class ConnectedThread extends Thread {
    private static final String TAG = "ConnectedThread";
    private final BluetoothSocket socket;
    private final InputStream inStream;
    private final OutputStream outStream;
    private final ConnectListener connListener;

    /**
     * Initialize the thread.
     * Author: LCY
     *
     * @param socket the socket of the connection
     * @param connListener a connection listener
     */
    public ConnectedThread(BluetoothSocket socket, ConnectListener connListener) {
        this.socket = socket;
        this.connListener = connListener;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            connListener.onError(ConnectListener.ERR_STREAM_CREATE, e);
        }
        inStream = tmpIn;
        outStream = tmpOut;
    }

    /**
     * Keep listening to the InputStream until an exception occurs.
     * Author: LCY
     */
    @Override
    public void run() {
        if (inStream == null || outStream == null) {
            return;
        }
        byte[] buffer = new byte[1024];
        connListener.onDataChannelEstablished();
        Log.d(TAG, "Open input stream...");
        while (true) {
            try {
                int bytesCnt = inStream.read(buffer);
                connListener.onReceive(bytesCnt, buffer);
            } catch (IOException e) {
                Log.e(TAG, "Error:", e);
                connListener.onError(ConnectListener.ERR_STREAM_READ, e);
                break;
            }
        }
        Log.d(TAG, "Thread ended.");
    }

    /**
     * Write data to remote device.
     * Author: LCY
     *
     * @param data the data to be sent
     */
    public void write(byte[] data) {
        try {
            Log.d(TAG, "Data of size " + data.length + " will be sent.");
            outStream.write(data);
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            connListener.onError(ConnectListener.ERR_STREAM_WRITE, e);
        }
    }

    /**
     * Shutdown the connection.
     * Author: LCY
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
