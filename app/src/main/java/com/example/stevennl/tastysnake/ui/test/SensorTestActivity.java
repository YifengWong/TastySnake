package com.example.stevennl.tastysnake.ui.test;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.model.Direction;
import com.example.stevennl.tastysnake.util.sensor.SensorController;

public class SensorTestActivity extends AppCompatActivity {
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

        initLayout();

        SensorController.setContext(getApplicationContext());
        sController = SensorController.getInstance();
        sController.registerSensor();
        sensorHandler = new Handler();
        sensorHandler.post(sensorRunnable);
    }

    private void initLayout() {
        textShow = (TextView) findViewById(R.id.text_show);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorHandler.removeCallbacks(sensorRunnable);
        sController.unregisterSensor();
    }
}
