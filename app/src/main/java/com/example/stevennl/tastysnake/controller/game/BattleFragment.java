package com.example.stevennl.tastysnake.controller.game;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.bluetooth.BluetoothManager;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnDataReceiveListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnErrorListener;

import java.lang.ref.WeakReference;

/**
 * Game battle page.
 */
public class BattleFragment extends Fragment {
    private static final String TAG = "BattleFragment";

    private GameActivity act;

    private SafeHandler handler;
    private BluetoothManager manager;
    private SendThread sendThread;

    private TextView infoTxt;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new SafeHandler(this);
        initManager();
        initThread();
        initDataListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_battle, container, false);
        initInfoTxt(v);
        initTestBtn(v);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.stopConnect();
    }

    private void initManager() {
        manager = BluetoothManager.getInstance();
        manager.setErrorListener(new OnErrorListener() {
            @Override
            public void onError(int code, Exception e) {
                Log.e(TAG, "Error code: " + code, e);
                errHandle(code);
            }
        });
    }

    private void initThread() {
        sendThread = new SendThread();
        sendThread.start();
    }

    private void initDataListener() {
        manager.setDataListener(new OnDataReceiveListener() {
            @Override
            public void onReceive(int bytesCount, byte[] data) {
                Log.d(TAG, "Receive " + bytesCount + " bytes.");
                handler.obtainMessage(SafeHandler.MSG_RECV_DATA, bytesCount, 0).sendToTarget();
            }
        });
    }

    private void initInfoTxt(View v) {
        infoTxt = (TextView) v.findViewById(R.id.battle_infoTxt);
    }

    private void initTestBtn(View v) {
        Button testBtn = (Button) v.findViewById(R.id.battle_testBtn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendThread.send();
            }
        });
    }

    /**
     * Return the info TextView.
     */
    public TextView getInfoTxt() {
        return infoTxt;
    }

    /**
     * Handle errors.
     * Notice that this method is called in a sub-thread.
     *
     * @param code The error code
     */
    private void errHandle(int code) {
        if (!isAdded()) return;
        switch (code) {
            case OnErrorListener.ERR_SOCKET_CLOSE:
            case OnErrorListener.ERR_STREAM_READ:
            case OnErrorListener.ERR_STREAM_WRITE:
                handler.obtainMessage(SafeHandler.MSG_ERR, getString(R.string.disconnect))
                        .sendToTarget();
                break;
            default:
                break;
        }
    }

    /**
     * A safe handler that circumvents memory leaks.
     */
    private static class SafeHandler extends Handler {
        private static final int MSG_RECV_DATA = 1;
        private static final int MSG_ERR = 2;
        private WeakReference<BattleFragment> fragment;

        private SafeHandler(BattleFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BattleFragment f = fragment.get();
            switch (msg.what) {
                case MSG_RECV_DATA:
                    if (f.isAdded()) {
                        f.getInfoTxt().append("Receive " + msg.arg1 + "\n");
                    }
                    break;
                case MSG_ERR:
                    if (f.isAdded()) {
                        CommonUtil.showToast(f.getActivity(), (String)msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Thread to send data to remote device.
     */
    private static class SendThread extends Thread {
        private SafeSendHandler sendHandler;

        @Override
        public void run() {
            Looper.prepare();
            sendHandler = new SafeSendHandler();
            Looper.loop();
        }

        /**
         * Send data to remote device.
         */
        private void send() {
            byte[] data = new byte[Config.BLUETOOTH_TEST_DATA_SIZE];
            for (int i = 0; i < Config.BLUETOOTH_TEST_DATA_SIZE; ++i) {
                data[i] = 'a';
            }
            sendHandler.obtainMessage(SafeSendHandler.MSG_SEND_DATA, data).sendToTarget();
        }

        /**
         * A safe handler that circumvents memory leaks.
         */
        private static class SafeSendHandler extends Handler {
            private static final int MSG_SEND_DATA = 1;
            private BluetoothManager manager_;

            private SafeSendHandler() {
                manager_ = BluetoothManager.getInstance();
            }

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SEND_DATA:
                        Log.d(TAG, "Receive MSG_SEND_DATA.");
                        byte[] data = (byte[]) msg.obj;
                        manager_.sendToRemote(data);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
