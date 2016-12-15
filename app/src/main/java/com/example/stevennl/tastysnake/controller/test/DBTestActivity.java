package com.example.stevennl.tastysnake.controller.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.model.BattleRecord;
import com.example.stevennl.tastysnake.model.Snake;
import com.example.stevennl.tastysnake.util.CommonUtil;

import java.util.ArrayList;

public class DBTestActivity extends AppCompatActivity {
    private static final String TAG = "DBTestActivity";
    private ArrayList<BattleRecord> records;
    private ArrayAdapter<BattleRecord> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);
        records = BattleRecord.getAll(this);
        initListView();
        initRemoveBtn();
        initGenBtn();
    }

    private void initListView() {
        ListView listView = (ListView)findViewById(R.id.db_test_listView);
        adapter = new ArrayAdapter<>(this, R.layout.item_list_record, records);
        listView.setAdapter(adapter);
    }

    private void initRemoveBtn() {
        Button removeBtn = (Button) findViewById(R.id.db_test_removeBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BattleRecord.removeAll(DBTestActivity.this);
                records.clear();
                records.addAll(BattleRecord.getAll(DBTestActivity.this));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initGenBtn() {
        Button testBtn = (Button) findViewById(R.id.db_test_genBtn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean win = (CommonUtil.randInt(2) == 0);
                Snake.MoveResult[] values = Snake.MoveResult.values();
                Snake.MoveResult cause = values[CommonUtil.randInt(values.length)];
                int duration = CommonUtil.randInt(180);
                int myLength = CommonUtil.randInt(50);
                int enemyLength = CommonUtil.randInt(50);
                new BattleRecord(win, cause, duration, myLength, enemyLength)
                        .save(DBTestActivity.this);
                records.clear();
                records.addAll(BattleRecord.getAll(DBTestActivity.this));
                adapter.notifyDataSetChanged();
            }
        });
    }
}