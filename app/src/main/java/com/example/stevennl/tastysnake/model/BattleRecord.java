package com.example.stevennl.tastysnake.model;

import android.content.Context;

import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.DBHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Record of one battle.
 * Author: QX
 */
public class BattleRecord {
    private Date timestamp;
    private boolean win;
    private Snake.MoveResult cause;
    private int duration;
    private int myLength;
    private int enemyLength;

    public BattleRecord() {
    }

    public BattleRecord(boolean win, Snake.MoveResult cause,
                        int duration, int myLength, int enemyLength) {
        timestamp = new Date(System.currentTimeMillis());
        this.win = win;
        this.cause = cause;
        this.duration = duration;
        this.myLength = myLength;
        this.enemyLength = enemyLength;
    }

    @Override
    public String toString() {
        return CommonUtil.formatDate(timestamp) + " " + win + " " + cause.name()
                + " " + duration + " " + myLength + " " + enemyLength;
    }

    /**
     * Insert this record to database.
     *
     * @param context The context
     */
    public void save(Context context) {
        DBHelper.getInstance(context).insert(this);
    }

    /**
     * Return all the BattleRecord in database.
     *
     * @param context The context
     */
    public static ArrayList<BattleRecord> getAll(Context context) {
        return DBHelper.getInstance(context).getAllRecords();
    }

    /**
     * Remove all the BattleRecord from database.
     *
     * @param context The context
     */
    public static void removeAll(Context context) {
        DBHelper.getInstance(context).removeAllRecords();
    }

    /**
     * Below are getters and setters
     */
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public Snake.MoveResult getCause() {
        return cause;
    }

    public void setCause(Snake.MoveResult cause) {
        this.cause = cause;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMyLength() {
        return myLength;
    }

    public void setMyLength(int myLength) {
        this.myLength = myLength;
    }

    public int getEnemyLength() {
        return enemyLength;
    }

    public void setEnemyLength(int enemyLength) {
        this.enemyLength = enemyLength;
    }
}
