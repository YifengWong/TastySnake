package com.example.stevennl.tastysnake.util.bluetooth.listener;

/**
 * Listener for state changes during a connection.
 * Author: LCY
 */
public interface OnStateChangedListener {
    void onClientSocketEstablished();
    void onServerSocketEstablished();
    void onDataChannelEstablished();
}
