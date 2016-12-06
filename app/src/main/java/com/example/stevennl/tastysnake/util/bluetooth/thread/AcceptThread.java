package com.example.stevennl.tastysnake.util.bluetooth.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnErrorListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnSocketEstablishedListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnStateChangedListener;

import java.io.IOException;
import java.util.UUID;

/**
 * Bluetooth server thread.
 * Author: LCY
 */
public class AcceptThread extends Thread {
    private static final String TAG = "AcceptThread";
    private final BluetoothServerSocket serverSocket;
    private final OnSocketEstablishedListener onSocketEstablishedListener;
    private final OnStateChangedListener stateListener;
    private final OnErrorListener errorListener;

    /**
     * Initialize the thread.
     *
     * @param adapter The bluetooth adapter
     */
    public AcceptThread(BluetoothAdapter adapter,
                        OnSocketEstablishedListener onSocketEstablishedListener,
                        OnStateChangedListener stateListener,
                        OnErrorListener errorListener) {
        super(TAG);
        this.onSocketEstablishedListener = onSocketEstablishedListener;
        this.stateListener = stateListener;
        this.errorListener = errorListener;
        BluetoothServerSocket tmp = null;
        try {
            tmp = adapter.listenUsingRfcommWithServiceRecord(
                    Config.BLUETOOTH_SERVICE_NAME,
                    UUID.fromString(Config.BLUETOOTH_UUID_STR));
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            errorListener.onError(OnErrorListener.ERR_SOCKET_CREATE, e);
        }
        serverSocket = tmp;
    }

    /**
     * Keep listening until exception occurs or a socket is returned
     */
    @Override
    public void run() {
        if (serverSocket == null) {
            return;
        }
        BluetoothSocket socket = null;
        Log.d(TAG, "Bluetooth server is listening...");
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Error:", e);
                errorListener.onError(OnErrorListener.ERR_SERVER_SOCKET_ACCEPT, e);
                break;
            }
            try {
                if (socket != null) {
                    onSocketEstablishedListener.onSocketEstablished(socket, stateListener, errorListener);
                    serverSocket.close();
                    break;
                }
            } catch (IOException e) {
                Log.e(TAG, "Error:", e);
                errorListener.onError(OnErrorListener.ERR_SOCKET_CLOSE, e);
                break;
            }
        }
        Log.d(TAG, "Thread ended.");
    }

    /**
     * Cancel the listening socket and cause the thread to finish.
     */
    public void cancel() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            errorListener.onError(OnErrorListener.ERR_SOCKET_CLOSE, e);
        }
    }
}
