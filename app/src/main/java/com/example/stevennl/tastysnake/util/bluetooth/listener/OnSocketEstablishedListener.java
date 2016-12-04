package com.example.stevennl.tastysnake.util.bluetooth.listener;

import android.bluetooth.BluetoothSocket;

/**
 * Listener for socket establishment of device connection .
 * Author: LCY
 */
public interface OnSocketEstablishedListener {

    /**
     * Called when a connection socket is established.
     *
     * @param socket The connection socket
     * @param stateListener A {@link OnStateChangedListener}
     * @param errorListener A {@link OnErrorListener}
     */
    void onSocketEstablished(BluetoothSocket socket,
                             OnStateChangedListener stateListener,
                             OnErrorListener errorListener);
}
