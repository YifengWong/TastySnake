package com.example.stevennl.tastysnake.ui.test;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stevennl.tastysnake.Constants;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.util.bluetooth.BluetoothManager;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnStateChangedListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnDataReceiveListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnDiscoverListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothTestActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothTestActivity";
    private static final int REQ_BLUETOOTH_ENABLED = 1;
    private static final int REQ_BLUETOOTH_DISCOVERABLE = 2;

    private TextView infoTxt;
    private Button restartBtn;
    private EditText devNameTxt;
    private LinearLayout devLayout;

    private long begTime;

    private BluetoothManager manager = BluetoothManager.getInstance();
    private ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<>();
    private SafeHandler handler = new SafeHandler(this);
    private OnStateChangedListener stateListener = new OnStateChangedListener() {
        @Override
        public void onClientSocketEstablished() {
            handler.obtainMessage(SafeHandler.MSG_APPEND_C_ESTABLISH).sendToTarget();
        }

        @Override
        public void onServerSocketEstablished() {
            handler.obtainMessage(SafeHandler.MSG_APPEND_S_ESTABLISH).sendToTarget();
        }

        @Override
        public void onDataChannelEstablished() {
            handler.obtainMessage(SafeHandler.MSG_APPEND_DATA_CHANNEL_OK).sendToTarget();
        }

        @Override
        public void onError(int code, Exception e) {
            Log.e(TAG, "Error code: " + code);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);
        initViews();
        test();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reset();
    }

    private void reset() {
        infoTxt.setText("");
        restartBtn.setVisibility(View.GONE);
        devLayout.setVisibility(View.GONE);
        discoveredDevices.clear();
        manager.unregisterDiscoveryReceiver(this);
        manager.cancelDiscovery();
        manager.stopServer();
        manager.stopConnect();
    }

    private void initViews() {
        infoTxt = (TextView) findViewById(R.id.bluetooth_test_infoTxt);

        restartBtn = (Button) findViewById(R.id.bluetooth_test_restartBtn);
        restartBtn.setVisibility(View.GONE);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                test();
            }
        });

        devLayout = (LinearLayout) findViewById(R.id.bluetooth_test_devNameLayout);
        devLayout.setVisibility(View.GONE);
        devNameTxt = (EditText) findViewById(R.id.bluetooth_test_devTxt);
        Button confirmBtn = (Button) findViewById(R.id.bluetooth_test_confirmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String devName = devNameTxt.getText().toString();
                for (BluetoothDevice device : discoveredDevices) {
                    if (device.getName().equals(devName)) {
                        devLayout.setVisibility(View.GONE);
                        restartBtn.setVisibility(View.GONE);
                        appendInfo("\nConnecting \"" + devName + "\" ...");
                        manager.connectDeviceAsync(device, stateListener);
                        return;
                    }
                }
                CommonUtil.showToast(BluetoothTestActivity.this, "No such device found!");
            }
        });
    }

    /**
     * A safe handler that circumvents memory leaks.
     * Author: LCY
     */
    private static class SafeHandler extends Handler {
        private static final int MSG_APPEND_C_ESTABLISH = 1;
        private static final int MSG_APPEND_S_ESTABLISH = 2;
        private static final int MSG_APPEND_DATA_CHANNEL_OK = 3;
        private static final int MSG_APPEND_DATA = 4;
        private static final String EXTRA_DATA_CNT = "bytesCount";
        private static final String EXTRA_DATA_CONTENT = "data";
        private WeakReference<BluetoothTestActivity> activity;


        private SafeHandler(BluetoothTestActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BluetoothTestActivity act = activity.get();
            switch (msg.what) {
                case MSG_APPEND_C_ESTABLISH:
                    act.appendInfo("\nClient connection socket established.");
                    break;
                case MSG_APPEND_S_ESTABLISH:
                    act.appendInfo("\nServer connection socket established.");
                    break;
                case MSG_APPEND_DATA_CHANNEL_OK:
                    act.appendInfo("Data transfer channel established.");
                    act.testRemain3();
                    break;
                case MSG_APPEND_DATA:
                    Bundle bundle = msg.getData();
                    int bytesCnt = bundle.getInt(EXTRA_DATA_CNT);
                    byte[] data = bundle.getByteArray(EXTRA_DATA_CONTENT);
                    act.testRemain4(bytesCnt, data);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_BLUETOOTH_ENABLED:
                if (resultCode == RESULT_OK) {
                    appendInfo("Bluetooth enabled.");
                    testRemain1();
                } else if (resultCode == RESULT_CANCELED) {
                    appendInfo("Fail to enable bluetooth. Maybe you cancel it.");
                    showRestart();
                }
                break;
            case REQ_BLUETOOTH_DISCOVERABLE:
                if (resultCode == 30) {  // 30 seconds will be the result code
                    appendInfo("Your device can be discovered in 30 seconds.");
                    // Start bluetooth server
                    appendInfo("\nBluetooth server thread starts working...");
                    manager.runServerAsync(stateListener);
                } else if (resultCode == RESULT_CANCELED) {
                    appendInfo("Your device cannot be discovered.");
                }
                testRemain2();
                break;
            default:
                break;
        }
    }

    private void test() {
        // Bluetooth support check
        appendInfo("Bluetooth support check: " + (manager.isSupport() ? "pass" : "fail"));
        // Bluetooth enable check
        appendInfo("Bluetooth enable check: " + (manager.isEnabled() ? "pass" : "fail"));
        if (!manager.isEnabled()) {
            appendInfo("\nTry to enable bluetooth...");
            startActivityForResult(manager.getEnableIntent(), REQ_BLUETOOTH_ENABLED);
        } else {
            testRemain1();
        }
    }

    private void testRemain1() {
        // Ask user to make the device discoverable
        appendInfo("\nTry to make your device discoverable...");
        Intent discoverableIntent = manager.getDiscoverableIntent(30);  // Discoverable for 30 seconds
        startActivityForResult(discoverableIntent, REQ_BLUETOOTH_DISCOVERABLE);
    }

    private void testRemain2() {
        // Device discovery check
        manager.registerDiscoveryReceiver(this,
                new OnDiscoverListener() {
                    @Override
                    public void onDiscover(BluetoothDevice device) {
                        appendInfo("Name: " + device.getName() + " / MAC: " + device.getAddress());
                        discoveredDevices.add(device);
                    }
                });
        if (manager.startDiscovery()) {
            appendInfo("\nDiscover devices for 8 seconds...");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    manager.unregisterDiscoveryReceiver(BluetoothTestActivity.this);
                    manager.cancelDiscovery();
                    appendInfo("Discover finished.");
                    if (discoveredDevices.isEmpty()) {
                        appendInfo("No device discovered.");
                    } else {
                        // Choose device to connect
                        devLayout.setVisibility(View.VISIBLE);
                    }
                    showRestart();
                    appendInfo("\nDo not click the restart button if you want to be connected.");
                }
            }, 8000);
        } else {
            appendInfo("\nFail to start device discovery.");
            showRestart();
        }
    }

    private void testRemain3() {
        // Bonded devices check
        Set<BluetoothDevice> bondedDevices = manager.getBondedDevices();
        appendInfo("\nSearching bonded devices...");
        for (BluetoothDevice device : bondedDevices) {
            appendInfo("Name: " + device.getName() + " / MAC: " + device.getAddress());
        }
        appendInfo("Searching finished.");

        // Data transfer check
        manager.setDataListener(new OnDataReceiveListener() {
            @Override
            public void onReceive(int bytesCount, byte[] data) {
                Bundle bundle = new Bundle();
                bundle.putInt(SafeHandler.EXTRA_DATA_CNT, bytesCount);
                bundle.putByteArray(SafeHandler.EXTRA_DATA_CONTENT, data);
                Message msg = handler.obtainMessage(SafeHandler.MSG_APPEND_DATA);
                msg.setData(bundle);
                msg.sendToTarget();
            }
        });
        appendInfo("\nSending data of " + Constants.BLUETOOTH_TEST_DATA_SIZE + " bytes to remote device...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] data = generateData();
                begTime = System.currentTimeMillis();
                manager.sendToRemote(data);
            }
        }).start();
    }

    private void testRemain4(int bytesCnt, byte[] data) {
        long ms = System.currentTimeMillis() - begTime;
        appendInfo("\nReceive " + bytesCnt + " bytes. Time elapsed: " + ms + " ms.");
        showRestart();
    }

    private byte[] generateData() {
        byte[] data = new byte[Constants.BLUETOOTH_TEST_DATA_SIZE];
        for (int i = 0; i < Constants.BLUETOOTH_TEST_DATA_SIZE; ++i) {
            data[i] = 'a';
        }
        return data;
    }

    private void appendInfo(String info) {
        infoTxt.append(info + "\n");
    }

    private void showRestart() {
        restartBtn.setVisibility(View.VISIBLE);
    }
}
