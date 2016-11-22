package com.example.stevennl.tastysnake.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.ui.test.BluetoothTestActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBluetoothBtn();
    }

    private void initBluetoothBtn() {
        Button btn = (Button) findViewById(R.id.main_bluetooth_test_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BluetoothTestActivity.class));
            }
        });
    }
}
