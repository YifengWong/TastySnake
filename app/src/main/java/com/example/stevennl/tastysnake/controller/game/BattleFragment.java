package com.example.stevennl.tastysnake.controller.game;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.controller.game.thread.RecvThread;
import com.example.stevennl.tastysnake.controller.game.thread.SendThread;
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
 * Author: LCY
 */
public class BattleFragment extends Fragment {
    private static final String TAG = "BattleFragment";

    private GameActivity act;

    private Timer timer;
    private SafeHandler handler;
    private BluetoothManager manager;
    private SensorController sensorCtrl;

    private SendThread sendThread;
    private RecvThread recvThread;

    private DrawableGrid grid;
    private TextView timeTxt;
    private TextView roleTxt;
    private TextView prepareTimeTxt;
    private Button restartBtn;

    private Map map;
    private Snake mySnake;
    private Snake enemySnake;
    private Snake.Type type;  // Distinguish server/client

    private int timeRemain;
    private boolean attack;
    private Snake.Type nextAttacker = Snake.Type.CLIENT;  // Attacker of the next round

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
        attack = isServer();  // Default attacker is SERVER snake
        initHandler();
        initSnake();
        initManager();
        initSensor();
        initDataTransferThread();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_battle, container, false);
        initGrid(v);
        initTimeTxt(v);
        initRoleTxt(v);
        initRestartBtn(v);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prepare();
            }
        }, Config.DELAY_BATTLE_FRAGMENT);
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
        sendThread.quitSafely();
        recvThread.quitSafely();
        stopGame(false);
    }

    private void initHandler() {
        handler = new SafeHandler(this);
    }

    private void initSnake() {
        map = Map.gameMap();
        mySnake = new Snake(type, map, Config.COLOR_SNAKE_MY);
        Snake.Type enemyType = isServer() ? Snake.Type.CLIENT : Snake.Type.SERVER;
        enemySnake = new Snake(enemyType, map, Config.COLOR_SNAKE_ENEMY);
    }

    private void initManager() {
        manager = BluetoothManager.getInstance();
        manager.setErrorListener(new OnErrorListener() {
            @Override
            public void onError(int code, Exception e) {
                Log.e(TAG, "Error code: " + code, e);
                if (isAdded()) {
                    handleErr(code);
                }
            }
        });
        manager.setDataListener(new OnDataReceiveListener() {
            @Override
            public void onReceive(int bytesCount, byte[] data) {
                Log.d(TAG, "Receive: " + bytesCount + " bytes. Cnt: " + (++recvCnt));
                if (recvThread != null && recvThread.isAlive()) {
                    recvThread.recv(new Packet(data));
                }
            }
        });
    }

    private void initSensor() {
        sensorCtrl = SensorController.getInstance(act);
    }

    private void initDataTransferThread() {
        sendThread = new SendThread();
        recvThread = new RecvThread(new RecvThread.OnPacketReceiveListener() {
            @Override
            public void onPacketReceive(Packet pkt) {
                switch (pkt.getType()) {
                    case FOOD_LENGTHEN:
                        map.createFood(pkt.getFoodX(), pkt.getFoodY(), true);
                        break;
                    case FOOD_SHORTEN:
                        map.createFood(pkt.getFoodX(), pkt.getFoodY(), false);
                        break;
                    case DIRECTION:
                        Direction direc = pkt.getDirec();
                        Snake.MoveResult res = enemySnake.move(direc);
                        handleMoveResult(enemySnake, res);
                        break;
                    case RESTART:
                        attack = (type == pkt.getAttacker());
                        handler.obtainMessage(SafeHandler.MSG_RESTART).sendToTarget();
                        break;
                    case TIME:
                        timeRemain = pkt.getTime();
                        handler.obtainMessage(SafeHandler.MSG_TIME).sendToTarget();
                        break;
                    case WIN:
                        Snake.Type winner = pkt.getWinner();
                        String infoStr = (winner == type ? getString(R.string.win) : getString(R.string.lose));
                        handler.obtainMessage(SafeHandler.MSG_TOAST, infoStr).sendToTarget();
                        stopGame(false);
                    default:
                        break;
                }
            }
        });
        sendThread.start();
        recvThread.start();
    }

    private void initGrid(View v) {
        grid = (DrawableGrid) v.findViewById(R.id.battle_grid);
        grid.setVisibility(View.GONE);
        grid.setBgColor(Config.COLOR_MAP_BG);
        grid.setMap(map);
    }

    private void initTimeTxt(View v) {
        prepareTimeTxt = (TextView) v.findViewById(R.id.battle_prepareTimeTxt);
        timeTxt = (TextView) v.findViewById(R.id.battle_timeTxt);
        timeRemain = Config.TIME_ATTACK - 1;
        updateTimeTxt();
    }

    private void initRoleTxt(View v) {
        roleTxt = (TextView) v.findViewById(R.id.battle_roleTxt);
        updateRoleTxt();
    }

    private void initRestartBtn(View v) {
        restartBtn = (Button) v.findViewById(R.id.battle_restartBtn);
        restartBtn.setVisibility(View.GONE);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendThread.send(Packet.restart(nextAttacker));
                v.setVisibility(View.GONE);
                attack = (type == nextAttacker);
                nextAttacker = (nextAttacker == Snake.Type.SERVER ? Snake.Type.CLIENT : Snake.Type.SERVER);
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
        grid.setMap(map);
        timeRemain = Config.TIME_ATTACK - 1;
        updateTimeTxt();
        updateRoleTxt();
        prepare();
    }

    /**
     * Preparation before starting the game.
     */
    private void prepare() {
        if (isAdded()) {
            grid.setVisibility(View.VISIBLE);
            prepareTimeTxt.setVisibility(View.VISIBLE);
            prepareTimeTxt.setText("");
            if (timer == null) {
                timer = new Timer();
            }
            timer.schedule(new TimerTask() {
                private int prepareTimeRemain = Config.TIME_GAME_PREPARE;
                private final String startStr = getString(R.string.game_start);

                @Override
                public void run() {
                    if (prepareTimeTxt.getText().toString().equals(startStr)) {
                        handler.obtainMessage(SafeHandler.MSG_HIDE_PREPARE_TXT).sendToTarget();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                startGame();
                            }
                        });
                    } else if (prepareTimeRemain == 0) {
                        handler.obtainMessage(SafeHandler.MSG_UPDATE_PREPARE_TIME, startStr)
                                .sendToTarget();
                    } else {
                        handler.obtainMessage(SafeHandler.MSG_UPDATE_PREPARE_TIME,
                                String.valueOf(prepareTimeRemain--)).sendToTarget();
                    }
                }
            }, 0, 1000);
        }
    }

    /**
     * Start the game.
     */
    private void startGame() {
        stopTimer();
        if (timer == null) {
            timer = new Timer();
        }
        if (isServer()) {
            startCreateFood();
            startTiming();
        }
        startMove();
    }

    /**
     * Start a thread to create food.
     */
    private void startCreateFood() {
        timer.schedule(new TimerTask() {
            private boolean lengthen = false;

            @Override
            public void run() {
                Pos food = map.createFood(lengthen = true);
                sendThread.send(Packet.food(food.getX(), food.getY(), lengthen));
            }
        }, 0, Config.INTERVAL_FOOD);
    }

    /**
     * Start a thread to calculate remaining time.
     */
    private void startTiming() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendThread.send(Packet.time(timeRemain));
                handler.obtainMessage(SafeHandler.MSG_TIME).sendToTarget();
            }
        }, 0, 1000);
    }

    /**
     * Stat a thread to move 'mySnake'.
     */
    private void startMove() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Direction direc = sensorCtrl.getDirection();
                sendThread.send(Packet.direction(direc));
                Snake.MoveResult res = mySnake.move(direc);
                handleMoveResult(mySnake, res);
            }
        }, 0, Config.INTERVAL_MOVE);
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
        switch (result) {
            case SUC:
                break;
            case SUICIDE:
            case OUT:
                if (isServer()) {
                    Snake.Type winner = (snake == mySnake ? enemySnake.getType() : type);
                    sendThread.send(Packet.win(winner));
                    String infoStr = (type == winner ? getString(R.string.win) : getString(R.string.lose));
                    handler.obtainMessage(SafeHandler.MSG_TOAST, infoStr).sendToTarget();
                }
                stopGame(true);
                break;
            case HIT_ENEMY:
                if (isServer()) {
                    Snake.Type winner = (attack ? type : enemySnake.getType());
                    sendThread.send(Packet.win(winner));
                    String infoStr = (attack ? getString(R.string.win) : getString(R.string.lose));
                    handler.obtainMessage(SafeHandler.MSG_TOAST, infoStr).sendToTarget();
                    stopGame(true);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Stop the game.
     *
     * @param showRestart If true, the restart button will be shown, false otherwise
     */
    private void stopGame(boolean showRestart) {
        stopTimer();
        if (showRestart && isServer()) {
            handler.obtainMessage(SafeHandler.MSG_SHOW_RESTART).sendToTarget();
        }
    }

    /**
     * Stop the timer. (thread-safe)
     */
    private final Object stopTimerLock = new Object();
    private void stopTimer() {
        synchronized (stopTimerLock) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    /**
     * Update remaining time TextView.
     */
    private void updateTimeTxt() {
        String timeStr = String.valueOf(timeRemain);
        if (timeRemain / 10 == 0) {
            timeStr = "0" + timeStr;
        }
        timeTxt.setText(String.format(getString(R.string.switch_role_remain), timeStr));
    }

    /**
     * Update the role TextView.
     */
    private void updateRoleTxt() {
        roleTxt.setText(CommonUtil.getAttackStr(act, attack));
    }

    /**
     * Return true if current device is the bluetooth server.
     */
    private boolean isServer() {
        return type == Snake.Type.SERVER;
    }

    /**
     * Handle exceptions.
     *
     * @param code The error code
     */
    private void handleErr(int code) {
        switch (code) {
            case OnErrorListener.ERR_SOCKET_CLOSE:
            case OnErrorListener.ERR_STREAM_READ:
            case OnErrorListener.ERR_STREAM_WRITE:
                handler.obtainMessage(SafeHandler.MSG_TOAST, getString(R.string.disconnect)).sendToTarget();
                stopGame(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isAdded()) {
                            act.replaceFragment(new HomeFragment(), true);
                        }
                    }
                }, 500);
                break;
            default:
                break;
        }
    }

    /**
     * A safe handler that circumvents memory leaks.
     */
    private static class SafeHandler extends Handler {
        private static final int MSG_TOAST = 2;
        private static final int MSG_RESTART = 3;
        private static final int MSG_TIME = 4;
        private static final int MSG_SHOW_RESTART = 5;
        private static final int MSG_UPDATE_PREPARE_TIME = 6;
        private static final int MSG_HIDE_PREPARE_TXT = 7;
        private static final int MSG_UPDATE_ROLE = 8;
        private WeakReference<BattleFragment> fragment;

        private SafeHandler(BattleFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            BattleFragment f = fragment.get();
            switch (msg.what) {
                case MSG_TOAST:
                    if (f.isAdded()) {
                        CommonUtil.showToast(f.act, (String)msg.obj);
                    }
                    break;
                case MSG_RESTART:
                    if (f.isAdded()) {
                        f.restart();
                    }
                    break;
                case MSG_TIME:
                    if (f.isAdded()) {
                        if (f.timeRemain == -1) {
                            f.attack = !f.attack;
                            f.timeRemain = Config.TIME_ATTACK - 1;
                            f.updateRoleTxt();
                        }
                        f.updateTimeTxt();
                        if (f.isServer()) {
                            --f.timeRemain;
                        }
                    }
                    break;
                case MSG_SHOW_RESTART:
                    if (f.isAdded()) {
                        f.restartBtn.setVisibility(View.VISIBLE);
                    }
                    break;
                case MSG_UPDATE_PREPARE_TIME:
                    if (f.isAdded()) {
                        f.prepareTimeTxt.setText((String)msg.obj);
                    }
                    break;
                case MSG_HIDE_PREPARE_TXT:
                    if (f.isAdded()) {
                        f.prepareTimeTxt.setVisibility(View.GONE);
                    }
                    break;
                case MSG_UPDATE_ROLE:
                    if (f.isAdded()) {
                        f.updateRoleTxt();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
