package com.example.stevennl.tastysnake.util.bluetooth.thread;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.stevennl.tastysnake.util.bluetooth.listener.OnStateChangedListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnDataReceiveListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Thread to control a connected socket to implement data transfer.
 * Author: LCY
 */
public class ConnectedThread extends Thread {
    private static final String TAG = "ConnectedThread";
    private final BluetoothSocket socket;
    private final InputStream inStream;
    private final OutputStream outStream;
    private final OnStateChangedListener stateListener;
    private OnDataReceiveListener dataListener;

    /**
     * Set dataListener field.
     */
    public void setDataListener(OnDataReceiveListener dataListener) {
        this.dataListener = dataListener;
    }

    /**
     * Initialize the thread.
     *
     * @param socket The socket of the connection
     */
    public ConnectedThread(BluetoothSocket socket, OnStateChangedListener stateListener) {
        this.socket = socket;
        this.stateListener = stateListener;
        this.dataListener = null;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            stateListener.onError(OnStateChangedListener.ERR_STREAM_CREATE, e);
        }
        inStream = tmpIn;
        outStream = tmpOut;
    }

    /**
     * Keep listening to the InputStream until an exception occurs.
     */
    @Override
    public void run() {
        if (inStream == null || outStream == null) {
            return;
        }
        byte[] buffer = new byte[1024];
        stateListener.onDataChannelEstablished();
        Log.d(TAG, "Open input stream...");
        while (true) {
            try {
                int bytesCnt = inStream.read(buffer);
                if (dataListener != null) {
                    dataListener.onReceive(bytesCnt, buffer);
                }
            } catch (IOException e) {
                Log.e(TAG, "Error:", e);
                stateListener.onError(OnStateChangedListener.ERR_STREAM_READ, e);
                break;
            }
        }
        Log.d(TAG, "Thread ended.");
    }

    /**
     * Write data to remote device.
     *
     * @param data The data to be sent
     */
    public void write(byte[] data) {
        try {
            Log.d(TAG, "Data of size " + data.length + " will be sent.");
            outStream.write(data);
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            stateListener.onError(OnStateChangedListener.ERR_STREAM_WRITE, e);
        }
    }

    /**
     * Shutdown the connection.
     */
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            stateListener.onError(OnStateChangedListener.ERR_SOCKET_CLOSE, e);
        }
    }
}
