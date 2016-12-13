package com.example.stevennl.tastysnake.controller.game;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.bluetooth.BluetoothManager;
import com.example.stevennl.tastysnake.widget.SnakeImage;

/**
 * Home page.
 * Author: LCY
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private GameActivity act;
    private Handler handler;

    private Button startBtn;
    private SnakeImage mySnakeImg;
    private SnakeImage enemySnakeImg;
    private boolean started = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        initStartBtn(v);
        initSnakeImg(v);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded() && !started) {
                    mySnakeImg.startEnter(null);
                    enemySnakeImg.startEnter(null);
                }
            }
        }, Config.DELAY_HOME);
        return v;
    }

    private void initStartBtn(View v) {
        startBtn = (Button) v.findViewById(R.id.home_start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BluetoothManager.getInstance().isSupport()) {
                    CommonUtil.showToast(act, getString(R.string.bluetooth_not_support));
                } else if (!started) {
                    started = true;
                    mySnakeImg.cancelAnim();
                    enemySnakeImg.cancelAnim();
                    mySnakeImg.startExit(null);
                    enemySnakeImg.startExit(new SnakeImage.AnimationEndListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (isAdded()) {
                                act.replaceFragment(new ConnectFragment(), true);
                            }
                        }
                    });
                }
            }
        });
    }

    private void initSnakeImg(View v) {
        mySnakeImg = (SnakeImage) v.findViewById(R.id.home_mySnake_img);
        enemySnakeImg = (SnakeImage) v.findViewById(R.id.home_enemySnake_img);
    }

    /**
     * Called when the back button is pressed.
     */
    public void onBackPressed() {
        mySnakeImg.cancelAnim();
        enemySnakeImg.cancelAnim();
        mySnakeImg.startExit(null);
        enemySnakeImg.startExit(new SnakeImage.AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        act.finish();
                    }
                }, Config.DELAY_HOME_FINISH);
            }
        });
        startBtn.setClickable(false);
        startBtn.setText(R.string.bye);
    }
}
