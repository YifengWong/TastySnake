package com.example.stevennl.tastysnake.util.bluetooth.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.stevennl.tastysnake.Constants;
import com.example.stevennl.tastysnake.util.bluetooth.listener.SocketListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.ConnectListener;

import java.io.IOException;
import java.util.UUID;

/**
 * Bluetooth server thread.
 * Author: LCY
 */
public class AcceptThread extends Thread {
    private static final String TAG = "AcceptThread";
    private final BluetoothServerSocket serverSocket;
    private final SocketListener socketListener;
    private final ConnectListener connListener;

    /**
     * Initialize the thread.
     *
     * @param adapter the bluetooth adapter
     * @param socketListener a socket listener
     * @param connListener a connection listener
     */
    public AcceptThread(BluetoothAdapter adapter,
                        SocketListener socketListener,
                        ConnectListener connListener) {
        this.socketListener = socketListener;
        this.connListener = connListener;
        BluetoothServerSocket tmp = null;
        try {
            tmp = adapter.listenUsingRfcommWithServiceRecord(
                    Constants.BLUETOOTH_SERVICE_NAME,
                    UUID.fromString(Constants.BLUETOOTH_UUID_STR));
        } catch (IOException e) {
            Log.e(TAG, "Error:", e);
            connListener.onError(ConnectListener.ERR_SOCKET_CREATE, e);
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
                connListener.onError(ConnectListener.ERR_SERVER_SOCKET_ACCEPT, e);
                break;
            }
            try {
                if (socket != null) {
                    connListener.onServerSocketEstablished();
                    socketListener.onSocketEstablished(socket);
                    serverSocket.close();
                    break;
                }
            } catch (IOException e) {
                Log.e(TAG, "Error:", e);
                connListener.onError(ConnectListener.ERR_SOCKET_CLOSE, e);
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
            connListener.onError(ConnectListener.ERR_SOCKET_CLOSE, e);
        }
    }
}
