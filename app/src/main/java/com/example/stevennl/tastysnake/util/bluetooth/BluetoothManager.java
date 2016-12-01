package com.example.stevennl.tastysnake.util.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.stevennl.tastysnake.util.bluetooth.listener.OnDataReceiveListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnSocketEstablishedListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnStateChangedListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnDiscoverListener;
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
     * @param context The context
     * @param listener Called when a device has been found
    */
    public void registerDiscoveryReceiver(Context context, final OnDiscoverListener listener) {
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
     * @param context The context
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
     * @param stateListener_ A state listener
     */
    public void runServerAsync(OnStateChangedListener stateListener_) {
        stopServer();
        acceptThread = new AcceptThread(bluetoothAdapter, new OnSocketEstablishedListener() {
            @Override
            public void onSocketEstablished(BluetoothSocket socket,
                                            OnStateChangedListener stateListener) {
                stateListener.onServerSocketEstablished();
                manageConnectedSocket(socket, stateListener);
            }
        }, stateListener_);
        acceptThread.start();
    }

    /**
     * Connect a bluetooth server.
     *
     * @param device The device to connect
     * @param stateListener A state listener
     */
    public void connectDeviceAsync(BluetoothDevice device, OnStateChangedListener stateListener) {
        stopConnect();
        cancelDiscovery();  // Discovery process will slow down the connection
        connectThread = new ConnectThread(device, new OnSocketEstablishedListener() {
            @Override
            public void onSocketEstablished(BluetoothSocket socket,
                                            OnStateChangedListener stateListener) {
                stopServer();  // When connected, this device does not need to be server.
                stateListener.onClientSocketEstablished();
                manageConnectedSocket(socket, stateListener);
            }
        }, stateListener);
        connectThread.start();
    }

    /**
     * Stop current running bluetooth server.
     */
    public void stopServer() {
        if (acceptThread != null && acceptThread.isAlive()) {
            acceptThread.cancel();
        }
        acceptThread = null;
    }

    /**
     * Stop current running connection thread.
     */
    public void stopConnect() {
        if (connectThread != null && connectedThread.isAlive()) {
            connectThread.cancel();
        }
        connectThread = null;
        if (connectedThread != null && connectedThread.isAlive()) {
            connectedThread.cancel();
        }
        connectedThread = null;
    }

    /**
     * Send data to remote device.
     *
     * @param data The data to be send
     */
    public void sendToRemote(byte[] data) {
        if (connectedThread != null && connectedThread.isAlive()) {
            connectedThread.write(data);
        }
    }

    /**
     * Set an {@link OnDataReceiveListener} to receive data.
     */
    public void setDataListener(OnDataReceiveListener listener) {
        if (connectedThread != null && connectedThread.isAlive()) {
            connectedThread.setDataListener(listener);
        }
    }

    /**
     * Manage a connected socket asynchronously.
     *
     * @param socket A connected socket
     * @param connListener A connection listener
     */
    private void manageConnectedSocket(BluetoothSocket socket, OnStateChangedListener connListener) {
        connectedThread = new ConnectedThread(socket, connListener);
        connectedThread.start();
    }
}
