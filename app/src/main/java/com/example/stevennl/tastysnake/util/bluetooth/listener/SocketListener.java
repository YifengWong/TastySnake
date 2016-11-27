package com.example.stevennl.tastysnake.util.bluetooth.listener;

import android.bluetooth.BluetoothSocket;

/**
 * Listener for socket establishment of device connection .
 * Author: LCY
 */
public interface SocketListener {

    /**
     * Called when a connection socket is established.
     *
     * @param socket the connection socket
     */
    void onSocketEstablished(BluetoothSocket socket);
}
