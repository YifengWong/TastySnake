package com.example.stevennl.tastysnake.controller.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.model.BattleRecord;
import com.example.stevennl.tastysnake.util.DBHelper;

import java.util.ArrayList;

public class DBTestActivity extends AppCompatActivity {

    DBHelper testDB;
    ArrayList<BattleRecord> testRecords = new ArrayList<>();
    mAdapter adapter;
    ListView db_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbtest);

        db_listView = (ListView)findViewById(R.id.db_listView);

        testDB = new DBHelper(this);

//        testDB.insert(new BattleRecord());
//        testDB.insert(new BattleRecord("2016/12/13 09:13:30", true, Snake.MoveResult.HIT_ENEMY, 300, 5, 6));


        testRecords = testDB.getAllRecords();
        adapter = new mAdapter(this, testRecords);
        db_listView.setAdapter(adapter);
    }
}

class mAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<BattleRecord> mBattleRecord;

    public mAdapter(Context mContext, ArrayList<BattleRecord> mBattleRecord) {
        this.mContext = mContext;
        this.mBattleRecord = mBattleRecord;
    }

    @Override
    public int getCount() {
        return mBattleRecord.size();
    }

    @Override
    public Object getItem(int position) {
        if (mBattleRecord == null)
            return null;
        return mBattleRecord.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.db_test_records, parent, false);
        TextView timestamp = (TextView)convertView.findViewById(R.id.timestamp);
        TextView win = (TextView)convertView.findViewById(R.id.win);
        TextView cause = (TextView)convertView.findViewById(R.id.cause);
        TextView time = (TextView)convertView.findViewById(R.id.time);
        TextView myLength = (TextView)convertView.findViewById(R.id.myLength);
        TextView enemyLength = (TextView)convertView.findViewById(R.id.enemyLength);
        timestamp.setText(mBattleRecord.get(position).getTimestamp());
        String isWin = (mBattleRecord.get(position).isWin()) ? "True" : "False";
        win.setText(isWin);
        cause.setText(mBattleRecord.get(position).getCause().toString());
        time.setText(mBattleRecord.get(position).getTime() + "");
        myLength.setText(mBattleRecord.get(position).getMyLength() + "");
        enemyLength.setText(mBattleRecord.get(position).getEnemyLength() + "");
        return convertView;
    }
}

