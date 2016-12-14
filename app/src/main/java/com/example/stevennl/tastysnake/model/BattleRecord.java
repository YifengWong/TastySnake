package com.example.stevennl.tastysnake.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Record of one battle.
 * Author:
 */
public class BattleRecord {
    private String timestamp;
    private boolean win;
    private Snake.MoveResult cause;
    private int time;
    private int myLength;
    private int enemyLength;

    public BattleRecord() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        timestamp = df.format(date);
        win = false;
        cause = Snake.MoveResult.SUICIDE;
        time = 0;
        myLength = 3;
        enemyLength = 3;
    }

    public BattleRecord(String timestamp, boolean win, Snake.MoveResult cause, int time, int myLength, int enemyLength) {
        this.timestamp = timestamp;
        this.win = win;
        this.cause = cause;
        this.time = time;
        this.myLength = myLength;
        this.enemyLength = enemyLength;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public boolean isWin() {
        return win;
    }
    public Snake.MoveResult getCause() {
        return cause;
    }
    public int getTime() {
        return time;
    }
    public int getMyLength() {
        return myLength;
    }
    public int getEnemyLength() {
        return enemyLength;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public void setWin(boolean win) {
        this.win = win;
    }
    public void setCause(Snake.MoveResult cause) {
        this.cause = cause;
    }
    public void setTime(int time) {
        this.time = time;
    }
    public void setMyLength(int myLength) {
        this.myLength = myLength;
    }
    public void setEnemyLength(int enemyLength) {
        this.enemyLength = enemyLength;
    }
}
