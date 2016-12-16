package com.example.stevennl.tastysnake.controller.test;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.base.BaseActivity;
import com.example.stevennl.tastysnake.util.sensor.SensorController;

public class SensorTestActivity extends BaseActivity {
    private Handler sensorHandler;
    private Runnable sensorRunnable = new Runnable() {
        @Override
        public void run() {
            textShow.setText(sController.getDirection().toString());
            sensorHandler.postDelayed(sensorRunnable, 100);
        }
    };

    private SensorController sController;
    private TextView textShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_test);
        textShow = (TextView) findViewById(R.id.sensor_test_txt);

        sController = SensorController.getInstance(this);
        sController.register();
        sensorHandler = new Handler();
        sensorHandler.post(sensorRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorHandler.removeCallbacks(sensorRunnable);
        sController.unregister();
    }
}
