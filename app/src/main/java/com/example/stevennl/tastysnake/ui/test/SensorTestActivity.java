package com.example.stevennl.tastysnake.ui.test;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.util.sensor.SensorController;

public class SensorTestActivity extends AppCompatActivity {
    private Handler sensorHandler;

    private Runnable sensorRunnable = new Runnable() {
        @Override
        public void run() {
            // 控制界面
            int dir = sController.getDirection();
            switch (dir) {
                case SensorController.DIR_LEFT:
                    textShow.setText("Left");
                    break;
                case SensorController.DIR_RIGHT:
                    textShow.setText("Right");
                    break;
                case SensorController.DIR_UP:
                    textShow.setText("Up");
                    break;
                case SensorController.DIR_DOWN:
                    textShow.setText("Down");
                    break;
            }
            sensorHandler.postDelayed(sensorRunnable, 40);
        }
    };


    private SensorController sController;
    private TextView textShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_test);

        initLayout();

        sController = new SensorController(this);
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
