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
 * Manage database CRUD operation. Implemented as a singleton.
 * Author: QX
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static DBHelper instance = null;

    private static final String DB_NAME = "TastySnake.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_BATTLE_RECORD = "battle_record";
    private static final String[] TABLE_COL = new String[]{
            "timestamp", "win", "cause", "duration", "myLength", "enemyLength"};

    /**
     * Return the only instance.
     */
    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    /**
     * Initialize.
     */
    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BATTLE_RECORD
                + " (" + TABLE_COL[0] + " TEXT, "
                + TABLE_COL[1] + " TEXT, "
                + TABLE_COL[2] + " INTEGER, "
                + TABLE_COL[3] + " INTEGER, "
                + TABLE_COL[4] + " INTEGER, "
                + TABLE_COL[5] + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing
    }

    /**
     * Insert a battle record to database.
     */
    public void insert(BattleRecord record) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        String timestamp = CommonUtil.formatDate(record.getTimestamp());
        boolean win = record.isWin();
        int cause = record.getCause().ordinal();
        int duration = record.getDuration();
        int myLength = record.getMyLength();
        int enemyLength = record.getEnemyLength();
        cv.put(TABLE_COL[0], timestamp);
        cv.put(TABLE_COL[1], win);
        cv.put(TABLE_COL[2], cause);
        cv.put(TABLE_COL[3], duration);
        cv.put(TABLE_COL[4], myLength);
        cv.put(TABLE_COL[5], enemyLength);
        db.insert(TABLE_BATTLE_RECORD, null, cv);
        db.close();
    }

    /**
     * Return all battle records in database.
     */
    public ArrayList<BattleRecord> getAllRecords() {
        ArrayList<BattleRecord> db_records = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_BATTLE_RECORD, TABLE_COL, null, null, null, null, null);
        while (cursor.moveToNext()) {
            BattleRecord temp = new BattleRecord();
            temp.setTimestamp(CommonUtil.parseDateStr(cursor.getString(cursor.getColumnIndex(TABLE_COL[0]))));
            temp.setWin(cursor.getString(cursor.getColumnIndex(TABLE_COL[1])).equals("1"));
            temp.setCause(Snake.MoveResult.values()[cursor.getInt(cursor.getColumnIndex(TABLE_COL[2]))]);
            temp.setDuration(cursor.getInt(cursor.getColumnIndex(TABLE_COL[3])));
            temp.setMyLength(cursor.getInt(cursor.getColumnIndex(TABLE_COL[4])));
            temp.setEnemyLength(cursor.getInt(cursor.getColumnIndex(TABLE_COL[5])));
            db_records.add(temp);
        }
        cursor.close();
        db.close();
        return db_records;
    }

    /**
     * Delete all battle records.
     */
    public void removeAllRecords() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_BATTLE_RECORD, null, new String[]{});
    }
}
