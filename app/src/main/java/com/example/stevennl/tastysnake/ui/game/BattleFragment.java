package com.example.stevennl.tastysnake.ui.game;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stevennl.tastysnake.Constants;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.util.bluetooth.BluetoothManager;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnDataReceiveListener;

import java.lang.ref.WeakReference;

public class BattleFragment extends Fragment {
    private static final String TAG = "BattleFragment";

    private GameActivity act;

    private Handler handler;
    private BluetoothManager manager;

    private TextView infoTxt;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
        handler = new SafeHandler(this);
        manager = BluetoothManager.getInstance();
        initDataListener();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] data = new byte[Constants.BLUETOOTH_TEST_DATA_SIZE];
                        for (int i = 0; i < Constants.BLUETOOTH_TEST_DATA_SIZE; ++i) {
                            data[i] = 'a';
                        }
                        manager.sendToRemote(data);
                    }
                }).start();
            }
        }, 1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_battle, container, false);
        initInfoTxt(v);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.stopConnect();
    }

    /**
     * Set an {@link OnDataReceiveListener} to receive data from remote device.
     * Notice that onReceive() is called in a sub-thread.
     */
    private void initDataListener() {
        manager.setDataListener(new OnDataReceiveListener() {
            @Override
            public void onReceive(int bytesCount, byte[] data) {
                Bundle bundle = new Bundle();
                bundle.putInt(SafeHandler.EXTRA_DATA_CNT, bytesCount);
                bundle.putByteArray(SafeHandler.EXTRA_DATA_CONTENT, data);
                Message msg = handler.obtainMessage(SafeHandler.MSG_NEW_DATA);
                msg.setData(bundle);
                msg.sendToTarget();
            }
        });
    }

    /**
     * Initialize info TextView.
     *
     * @param v The root view
     */
    private void initInfoTxt(View v) {
        infoTxt = (TextView) v.findViewById(R.id.battle_infoTxt);
    }

    /**
     * Return the info TextView.
     */
    public TextView getInfoTxt() {
        return infoTxt;
    }

    /**
     * A safe handler that circumvents memory leaks.
     * Author: LCY
     */
    private static class SafeHandler extends Handler {
        private static final int MSG_NEW_DATA = 1;
        private static final String EXTRA_DATA_CNT = "bytesCount";
        private static final String EXTRA_DATA_CONTENT = "data";
        private WeakReference<BattleFragment> fragment;

        private SafeHandler(BattleFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BattleFragment f = fragment.get();
            switch (msg.what) {
                case MSG_NEW_DATA:
                    if (f.isAdded()) {
                        Bundle bundle = msg.getData();
                        int bytesCnt = bundle.getInt(EXTRA_DATA_CNT);
                        byte[] data = bundle.getByteArray(EXTRA_DATA_CONTENT);
                        f.getInfoTxt().append("Receive " + bytesCnt + "\n");
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
