package com.example.stevennl.tastysnake.util.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.stevennl.tastysnake.util.bluetooth.listener.SocketListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.ConnectListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.DiscoverListener;
import com.example.stevennl.tastysnake.util.bluetooth.thread.AcceptThread;
import com.example.stevennl.tastysnake.util.bluetooth.thread.ConnectThread;
import com.example.stevennl.tastysnake.util.bluetooth.thread.ConnectedThread;

import java.util.Set;

/**
 * A class to control bluetooth discovery, connection and data transfer.
 * Implemented as a singleton.
 * Reference: https://developer.android.com/guide/topics/connectivity/bluetooth.html
 * Author: LCY
 */
public class BluetoothManager {
    private static final String TAG = "BluetoothManager";
    private static BluetoothManager instance;

    private BluetoothAdapter bluetoothAdapter;

    private BroadcastReceiver discoveryReceiver;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    /**
     * Initialize.
     */
    private BluetoothManager() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Return the only instance.
     */
    public static BluetoothManager getInstance() {
        if (instance == null) {
            instance = new BluetoothManager();
        }
        return instance;
    }

    /**
     * Return true if the device supports bluetooth.
     */
    public boolean isSupport() {
        return bluetoothAdapter != null;
    }

    /**
     * Return true if bluetooth is currently enabled and ready for use.
     */
    public boolean isEnabled() {
        return bluetoothAdapter.isEnabled();
    }

    /**
     * Return an intent that asks the user to enable bluetooth.
     * The intent should be used with startActivityForResult().
     */
    public Intent getEnableIntent() {
        return new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    }

    /**
     * Return the bonded devices set.
     */
    public Set<BluetoothDevice> getBondedDevices() {
        return bluetoothAdapter.getBondedDevices();
    }

    /**
     * Start device discovery. (keep discovering for about 12s)
     * The device discovered will be sent as broadcast.
     *
     * @return true on success, false on error
     */
    public boolean startDiscovery() {
        return bluetoothAdapter.startDiscovery();
    }

    /**
     * Cancel device discovery.
     */
    public void cancelDiscovery() {
        bluetoothAdapter.cancelDiscovery();
    }

    /**
     * Return true if bluetooth is discovering.
     */
    public boolean isDiscovering() {
        return bluetoothAdapter.isDiscovering();
    }

    /**
     * Register a BroadcastReceiver to handle discovered devices.
     *
     * @param context the context
     * @param listener be called when a device has been found
    */
    public void registerDiscoveryReceiver(Context context, final DiscoverListener listener) {
        if (discoveryReceiver == null) {
            discoveryReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        BluetoothDevice device = intent
                                .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        listener.onDiscover(device);
                    }
                }
            };
        }
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(discoveryReceiver, filter);
    }

    /**
     * Unregister the BroadcastReceiver to handle discovered devices.
     *
     * @param context the context
     */
    public void unregisterDiscoveryReceiver(Context context) {
        if (discoveryReceiver != null) {
            context.unregisterReceiver(discoveryReceiver);
            discoveryReceiver = null;
        }
    }

    /**
     * Return an intent that asks the user to make the device discoverable.
     * The intent should be used with startActivityForResult().
     *
     * @param discoverableSeconds the time(seconds) that the device can be discovered
     */
    public Intent getDiscoverableIntent(int discoverableSeconds) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, discoverableSeconds);
        return intent;
    }

    /**
     * Run a bluetooth server thread to listen to the connection request.
     *
     * @param connListener a connection listener
     */
    public void runServerAsync(final ConnectListener connListener) {
        acceptThread = new AcceptThread(bluetoothAdapter, new SocketListener() {
            @Override
            public void onSocketEstablished(BluetoothSocket socket) {
                manageConnectedSocket(socket, connListener);
            }
        }, connListener);
        acceptThread.start();
    }

    /**
     * Stop current running bluetooth server.
     */
    public void stopServer() {
        if (acceptThread != null && acceptThread.isAlive()) {
            acceptThread.cancel();
        }
    }

    /**
     * Connect a bluetooth server.
     *
     * @param device the device to connect
     * @param connListener a connection listener
     */
    public void connectDeviceAsync(BluetoothDevice device, final ConnectListener connListener) {
        cancelDiscovery();  // Discovery process will slow down the connection
        stopServer();  // When decide to connect another device, this device does not need to be server.
        connectThread = new ConnectThread(device, new SocketListener() {
            @Override
            public void onSocketEstablished(BluetoothSocket socket) {
                manageConnectedSocket(socket, connListener);
            }
        }, connListener);
        connectThread.start();
    }

    /**
     * Stop current connection.
     */
    public void stopConnection() {
        if (connectThread != null && connectThread.isAlive()) {
            connectThread.cancel();
        }
        if (connectedThread != null && connectedThread.isAlive()) {
            connectedThread.cancel();
        }
    }

    /**
     * Send data to remote device.
     *
     * @param data the data to be send
     */
    public void sendToRemote(byte[] data) {
        if (connectedThread != null && connectedThread.isAlive()) {
            connectedThread.write(data);
        }
    }

    /**
     * Manage a connected socket asynchronously.
     *
     * @param socket a connected socket
     * @param connListener a connection listener
     */
    private void manageConnectedSocket(BluetoothSocket socket, ConnectListener connListener) {
        connectedThread = new ConnectedThread(socket, connListener);
        connectedThread.start();
    }
}
