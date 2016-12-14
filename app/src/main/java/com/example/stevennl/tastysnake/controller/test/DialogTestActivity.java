package com.example.stevennl.tastysnake.controller.test;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.widget.HelpDialog;

public class DialogTestActivity extends AppCompatActivity {
    private static final String TAG = "DialogTestActivity";

    private HelpDialog helpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaglog_test);
        Button btn = (Button) findViewById(R.id.dialog_test_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.showToast(DialogTestActivity.this, "Click");
                helpDialog.show();
            }
        });

        helpDialog = new HelpDialog(this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Something
                Log.d("HelpDialog", "the listener called");
            }
        });
    }


}
