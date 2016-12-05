package com.example.stevennl.tastysnake.controller.game;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.controller.game.thread.DataTransferThread;
import com.example.stevennl.tastysnake.model.Map;
import com.example.stevennl.tastysnake.model.Packet;
import com.example.stevennl.tastysnake.model.Pos;
import com.example.stevennl.tastysnake.model.Snake;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.bluetooth.BluetoothManager;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnDataReceiveListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnErrorListener;
import com.example.stevennl.tastysnake.widget.DrawableGrid;

import java.lang.ref.WeakReference;

/**
 * Game battle page.
 */
public class BattleFragment extends Fragment {
    private static final String TAG = "BattleFragment";

    private GameActivity act;

    private SafeHandler handler;
    private BluetoothManager manager;
    private DataTransferThread dataThread;

    private DrawableGrid grid;

    private Snake.Type type;
    private Map map;
    private Snake snakeServer;
    private Snake snakeClient;

    /**
     * Create a {@link BattleFragment} with a given snake type.
     */
    public static BattleFragment newInstance(Snake.Type type) {
        BattleFragment fragment = new BattleFragment();
        fragment.type = type;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "Snake type: " + type.name());
        act = (GameActivity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHandler();
        initSnakes();
        initManager();
        initThread();
        initDataListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_battle, container, false);
        initGrid(v);
        initTestBtn(v);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.stopConnect();
    }

    private void initHandler() {
        handler = new SafeHandler(this);
    }

    private void initSnakes() {
        map = Map.gameMap();
        snakeServer = new Snake(Snake.Type.SERVER, map);
//        snakeClient = new Snake(Snake.Type.CLIENT, map);
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
        dataThread = new DataTransferThread(type == Snake.Type.SERVER ? snakeServer : snakeServer);
        dataThread.start();
    }

    private void initDataListener() {
        manager.setDataListener(new OnDataReceiveListener() {
            @Override
            public void onReceive(int bytesCount, byte[] data) {
                Log.d(TAG, "Receive: " + bytesCount + " bytes.");
                dataThread.recv(data);
            }
        });
    }

    private void initGrid(View v) {
        grid = (DrawableGrid) v.findViewById(R.id.battle_grid);
        grid.setMap(map);
        grid.setBgColor(Config.COLOR_MAP_BG);
        grid.setVisibility(View.INVISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                grid.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

    private void initTestBtn(View v) {
        Button testBtn = (Button) v.findViewById(R.id.battle_testBtn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == Snake.Type.SERVER) {
                    boolean lengthen = (CommonUtil.randInt(2) == 0);
                    Pos food = map.createFood(lengthen);
                    dataThread.send(Packet.food(food.getX(), food.getY(), lengthen));
                }
            }
        });
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
        private static final int MSG_ERR = 1;
        private WeakReference<BattleFragment> fragment;

        private SafeHandler(BattleFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BattleFragment f = fragment.get();
            switch (msg.what) {
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
}
