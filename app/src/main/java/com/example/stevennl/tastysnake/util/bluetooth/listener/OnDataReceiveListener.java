package com.example.stevennl.tastysnake.util.bluetooth.listener;

/**
 * Listener for data transfer.
 * Author: LCY
 */
public interface OnDataReceiveListener {

    /**
     * Called when receiving data from remote device
     *
     * @param bytesCount The number of bytes received
     * @param data The data received
     */
    void onReceive(int bytesCount, byte[] data);
}
