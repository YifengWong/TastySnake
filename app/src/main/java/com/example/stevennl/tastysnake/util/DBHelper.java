package com.example.stevennl.tastysnake.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.stevennl.tastysnake.model.BattleRecord;
import com.example.stevennl.tastysnake.model.Snake;

import java.util.ArrayList;

/**
 * Manage database CRUD operation.
 * Author:
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final String DB_NAME = "TastySnake";
    private static final String TABLE_NAME = "battle_record";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE if not exists "
                + TABLE_NAME
                + "(_id INTEGER PRIMARY KEY, timestamp TEXT, win TEXT, cause INTEGER, time INTEGER, myLength INTEGER, enemyLength INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    /**
     * Insert a battle record to database.
     */
    public void insert(BattleRecord record) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        String timestamp = record.getTimestamp();
        boolean win = record.isWin();
        int cause = record.getCause().ordinal();
        int time = record.getTime();
        int myLength = record.getMyLength();
        int enemyLength = record.getEnemyLength();
        cv.put("timestamp", timestamp);
        cv.put("win", win);
        cv.put("cause", cause);
        cv.put("time", time);
        cv.put("myLength", myLength);
        cv.put("enemyLength", enemyLength);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    /**
     * Return all battle records in database.
     */
    public ArrayList<BattleRecord> getAllRecords() {
        ArrayList<BattleRecord> db_records = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{"timestamp", "win", "cause", "time", "myLength", "enemyLength"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            BattleRecord temp = new BattleRecord();
            temp.setTimestamp(cursor.getString(cursor.getColumnIndex("timestamp")));
            temp.setWin(cursor.getString(cursor.getColumnIndex("win")).equals("1"));
            temp.setCause(Snake.MoveResult.values()[cursor.getInt(cursor.getColumnIndex("cause"))]);
            temp.setTime(cursor.getInt(cursor.getColumnIndex("time")));
            temp.setMyLength(cursor.getInt(cursor.getColumnIndex("myLength")));
            temp.setEnemyLength(cursor.getInt(cursor.getColumnIndex("enemyLength")));
            db_records.add(temp);
        }
        db.close();
        return db_records;
    }
}
