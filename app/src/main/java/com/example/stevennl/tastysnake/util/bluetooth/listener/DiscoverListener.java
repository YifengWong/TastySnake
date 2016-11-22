package com.example.stevennl.tastysnake.util.bluetooth.listener;

import android.bluetooth.BluetoothDevice;

/**
 * Listener for device discovery.
 * Author: LCY
 */
public interface DiscoverListener {

    /**
     * Called when a device is discovered.
     * Author: LCY
     *
     * @param device the discovered device
     */
    void onDiscover(BluetoothDevice device);
}
