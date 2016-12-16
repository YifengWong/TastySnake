package com.example.stevennl.tastysnake.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.base.BaseActivity;
import com.example.stevennl.tastysnake.controller.game.GameActivity;
import com.example.stevennl.tastysnake.controller.test.BluetoothTestActivity;
import com.example.stevennl.tastysnake.controller.test.DBTestActivity;
import com.example.stevennl.tastysnake.controller.test.DialogTestActivity;
import com.example.stevennl.tastysnake.controller.test.DrawableGridTestActivity;
import com.example.stevennl.tastysnake.controller.test.PacketTestActivity;
import com.example.stevennl.tastysnake.controller.test.RequestTestActivity;
import com.example.stevennl.tastysnake.controller.test.SensorTestActivity;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = (ViewGroup) findViewById(R.id.activity_main);
        initBtns();
    }

    @Override
    protected void onResume() {
        super.onResume();
        rootView.setBackgroundColor(Config.COLOR_MAP_BG);
    }

    private void initBtns() {
        Button bluetoothBtn = (Button) findViewById(R.id.main_bluetooth_test_btn);
        bluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BluetoothTestActivity.class));
            }
        });

        Button drawableGridBtn = (Button) findViewById(R.id.main_drawableGrid_test_btn);
        drawableGridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DrawableGridTestActivity.class));
            }
        });

        Button sensorBtn = (Button) findViewById(R.id.main_sensor_test_btn);
        sensorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SensorTestActivity.class));
            }
        });

        Button pktBtn = (Button) findViewById(R.id.main_pkt_test_btn);
        pktBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PacketTestActivity.class));
            }
        });

        Button dbBtn = (Button) findViewById(R.id.main_db_test_btn);
        dbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DBTestActivity.class));
            }
        });

        Button dialogBtn = (Button) findViewById(R.id.main_dialog_test_btn);
        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DialogTestActivity.class));
            }
        });

        Button reqBtn = (Button) findViewById(R.id.main_req_test_btn);
        reqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RequestTestActivity.class));
            }
        });

        Button gameBtn = (Button) findViewById(R.id.main_game_btn);
        gameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
    }
}
