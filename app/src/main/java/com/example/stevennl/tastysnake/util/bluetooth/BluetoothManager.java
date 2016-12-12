package com.example.stevennl.tastysnake.util.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.stevennl.tastysnake.util.bluetooth.listener.OnDataReceiveListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnErrorListener;
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

    private BroadcastReceiver discoverRecvr;

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
     * Return true if bluetooth is discovering, false otherwise.
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
        if (discoverRecvr == null) {
            discoverRecvr = new BroadcastReceiver() {
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
        context.registerReceiver(discoverRecvr, filter);
    }

    /**
     * Unregister the BroadcastReceiver to handle discovered devices.
     *
     * @param context The context
     */
    public void unregisterDiscoveryReceiver(Context context) {
        if (discoverRecvr != null) {
            context.unregisterReceiver(discoverRecvr);
            discoverRecvr = null;
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
     */
    public void runServerAsync(OnStateChangedListener stateListener_,
                               OnErrorListener errorListener_) {
        if (!isServerRunning()) {
            acceptThread = new AcceptThread(bluetoothAdapter, new OnSocketEstablishedListener() {
                @Override
                public void onSocketEstablished(BluetoothSocket socket,
                                                OnStateChangedListener stateListener,
                                                OnErrorListener errorListener) {
                    stateListener.onServerSocketEstablished();
                    manageConnectedSocket(socket, stateListener, errorListener);
                }
            }, stateListener_, errorListener_);
            acceptThread.start();
            Log.d(TAG, "Server running...");
        }
    }

    /**
     * Connect a bluetooth server.
     *
     * @param device The device to connect
     */
    public void connectDeviceAsync(BluetoothDevice device,
                                   OnStateChangedListener stateListener_,
                                   OnErrorListener errorListener_) {
        cancelDiscovery();  // Discovery process will slow down the connection
        if (!isClientConnecting()) {
            connectThread = new ConnectThread(device, new OnSocketEstablishedListener() {
                @Override
                public void onSocketEstablished(BluetoothSocket socket,
                                                OnStateChangedListener stateListener,
                                                OnErrorListener errorListener) {
                    stateListener.onClientSocketEstablished();
                    manageConnectedSocket(socket, stateListener, errorListener);
                }
            }, stateListener_, errorListener_);
            connectThread.start();
            Log.d(TAG, "Connecting...");
        }
    }

    /**
     * Manage a connected socket asynchronously.
     *
     * @param socket A connected socket
     */
    private void manageConnectedSocket(BluetoothSocket socket,
                                       OnStateChangedListener connListener,
                                       OnErrorListener errorListener) {
        connectedThread = new ConnectedThread(socket, connListener, errorListener);
        connectedThread.start();
    }

    /**
     * Return true if bluetooth server is running, false otherwise.
     */
    public boolean isServerRunning() {
        return acceptThread != null && acceptThread.isAlive();
    }

    /**
     * Return true if bluetooth client is connecting.
     */
    public boolean isClientConnecting() {
        return connectThread != null && connectThread.isAlive();
    }

    /**
     * Return true if bluetooth connection is established.
     */
    public boolean isConnected() {
        return connectedThread != null && connectedThread.isAlive();
    }

    /**
     * Stop current running bluetooth server.
     */
    public void stopServer() {
        if (isServerRunning()) {
            acceptThread.cancel();
        }
        acceptThread = null;
    }

    /**
     * Stop current running connection thread.
     */
    public void stopConnect() {
        if (isClientConnecting()) {
            connectThread.cancel();
        }
        connectThread = null;
        if (isConnected()) {
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
        if (isConnected()) {
            connectedThread.write(data);
        }
    }

    /**
     * Set an {@link OnDataReceiveListener} to receive data.
     */
    public void setDataListener(OnDataReceiveListener listener) {
        if (isConnected()) {
            connectedThread.setDataListener(listener);
        }
    }

    /**
     * Set an {@link OnErrorListener} after connected.
     */
    public void setErrorListener(OnErrorListener listener) {
        if (isConnected()) {
            connectedThread.setErrorListener(listener);
        }
    }
}
