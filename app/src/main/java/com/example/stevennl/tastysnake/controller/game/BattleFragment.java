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
import com.example.stevennl.tastysnake.model.Direction;
import com.example.stevennl.tastysnake.model.Map;
import com.example.stevennl.tastysnake.model.Packet;
import com.example.stevennl.tastysnake.model.Pos;
import com.example.stevennl.tastysnake.model.Snake;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.bluetooth.BluetoothManager;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnDataReceiveListener;
import com.example.stevennl.tastysnake.util.bluetooth.listener.OnErrorListener;
import com.example.stevennl.tastysnake.util.sensor.SensorController;
import com.example.stevennl.tastysnake.widget.DrawableGrid;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Game battle page.
 */
public class BattleFragment extends Fragment {
    private static final String TAG = "BattleFragment";

    private GameActivity act;

    private Timer timer;
    private SafeHandler handler;
    private BluetoothManager manager;
    private SensorController sensorCtrl;

    private DataTransferThread dataThread;

    private DrawableGrid grid;

    private Snake.Type type;
    private Map map;
    private Snake mySnake;
    private Snake enemySnake;

    // Debug fields
    private int recvCnt = 0;

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
        initSnake();
        initManager();
        initSensor();
        initDataTransferThread();
        initDataListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_battle, container, false);
        initGrid(v);
        initRestartBtn(v);
        prepare();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorCtrl.register();
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorCtrl.unregister();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.stopConnect();
        dataThread.quitSafely();
        stopGame();
    }

    private void initHandler() {
        handler = new SafeHandler(this);
    }

    private void initSnake() {
        map = Map.gameMap();
        if (type == Snake.Type.SERVER) {
            mySnake = new Snake(Snake.Type.SERVER, map);
            enemySnake = new Snake(Snake.Type.CLIENT, map);
        } else if (type == Snake.Type.CLIENT) {
            mySnake = new Snake(Snake.Type.CLIENT, map);
            enemySnake = new Snake(Snake.Type.SERVER, map);
        }
    }

    private void initManager() {
        manager = BluetoothManager.getInstance();
        manager.setErrorListener(new OnErrorListener() {
            @Override
            public void onError(int code, Exception e) {
                Log.e(TAG, "Error code: " + code, e);
                handleErr(code);
            }
        });
    }

    private void initSensor() {
        sensorCtrl = SensorController.getInstance(act);
    }

    private void initDataTransferThread() {
        dataThread = new DataTransferThread(new DataTransferThread.OnPacketReceiveListener() {
            @Override
            public void onPacketReceive(Packet pkt) {
                switch (pkt.getType()) {
                    case FOOD_LENGTHEN: {
                        Pos food = pkt.getFood();
                        map.createFood(food.getX(), food.getY(), true);
                        break;
                    }
                    case FOOD_SHORTEN: {
                        Pos food = pkt.getFood();
                        map.createFood(food.getX(), food.getY(), false);
                        break;
                    }
                    case DIRECTION: {
                        Direction direc= pkt.getDirec();
                        Snake.MoveResult res = enemySnake.move(direc);
                        handleMoveResult(enemySnake, res);
                        break;
                    }
                    case RESTART:
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                restart();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
        dataThread.start();
    }

    private void initDataListener() {
        manager.setDataListener(new OnDataReceiveListener() {
            @Override
            public void onReceive(int bytesCount, byte[] data) {
                Log.d(TAG, "Receive: " + bytesCount + " bytes. Cnt: " + (++recvCnt));
                dataThread.recv(new Packet(data));
            }
        });
    }

    private void initGrid(View v) {
        grid = (DrawableGrid) v.findViewById(R.id.battle_grid);
        grid.setMap(map);
        grid.setBgColor(Config.COLOR_MAP_BG);
    }

    private void initRestartBtn(View v) {
        Button restartBtn = (Button) v.findViewById(R.id.battle_restartBtn);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataThread.send(Packet.restart());
                restart();
            }
        });
    }

    /**
     * Restart the game.
     */
    private void restart() {
        stopTimer();
        initSnake();
        grid.setVisibility(View.INVISIBLE);
        grid.setMap(map);
        prepare();
    }

    /**
     * Preparation before starting the game.
     */
    private void prepare() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startGame();
            }
        }, 1000);  // Start the game after 1 second
    }

    /**
     * Start the game.
     */
    private void startGame() {
        grid.setVisibility(View.VISIBLE);
        if (type == Snake.Type.SERVER) {
            startCreateFood();
        }
        startMove();
    }

    /**
     * Start a thread to create food.
     */
    private void startCreateFood() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                boolean lengthen = (CommonUtil.randInt(2) == 0);
                Pos food = map.createFood(lengthen);
                dataThread.send(Packet.food(food.getX(), food.getY(), lengthen));
            }
        }, 0, Config.INTERVAL_FOOD);
    }

    /**
     * Stat a thread to move 'mySnake'.
     */
    private void startMove() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Direction direc = sensorCtrl.getDirection();
                dataThread.send(Packet.direction(direc));
                Snake.MoveResult res = mySnake.move(direc);
                handleMoveResult(mySnake, res);
            }
        }, 0, Config.INTERVAL_MOVE);
    }

    /**
     * Stop the game.
     */
    private void stopGame() {
        stopTimer();
    }

    /**
     * Stop the timer in order to stop creating food and moving 'mySnake'.
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Handle snake's move result.
     *
     * @param snake The snake who generated the move result
     * @param result The move result.
     */
    private void handleMoveResult(Snake snake, Snake.MoveResult result) {
        if (!isAdded()) {
            return;
        }
        String s = (snake == mySnake ? "My snake " : "Enemy snake ");
        switch (result) {
            case SUC:
                break;
            case SUICIDE:
            case HIT_ENEMY:
            case OUT:
                handler.obtainMessage(SafeHandler.MSG_TOAST, s + result.name()).sendToTarget();
                stopGame();
            default:
                break;
        }
    }

    /**
     * Handle errors.
     * Notice that this method is called in a sub-thread.
     *
     * @param code The error code
     */
    private void handleErr(int code) {
        if (!isAdded()) {
            return;
        }
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
        private static final int MSG_TOAST = 2;
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
                        CommonUtil.showToast(f.act, (String)msg.obj);
                        f.stopGame();
                    }
                    break;
                case MSG_TOAST:
                    if (f.isAdded()) {
                        CommonUtil.showToast(f.act, (String)msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
