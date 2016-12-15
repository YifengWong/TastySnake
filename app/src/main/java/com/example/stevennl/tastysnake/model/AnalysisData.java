package com.example.stevennl.tastysnake.model;

import android.content.Context;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.util.DBHelper;

import java.util.ArrayList;

/**
 * Data analysis parameters.
 * Author: QX
 */
public class AnalysisData {
    private static final String TAG = "AnalysisData";
    public int N;
    public int X;
    public int A;
    public int B;
    public int Y;
    public int C;
    public int D;
    public int T;
    public int L1;
    public int L2;
    public int W;
    public String P;

    /**
     * Create data to analyze.
     *
     * @param context The context
     */
    public static AnalysisData create(Context context) {
        // TODO Compute parameters using data from DB
        String[] rank = context.getResources().getStringArray(R.array.rank_array);
        AnalysisData data = new AnalysisData();
        int X = 0, A = 0, B = 0, Y = 0, C = 0, D = 0, T = 0, L1 = 0, L2 = 0;
        ArrayList<BattleRecord> records = BattleRecord.getAll(context);
        for (int i = 0; i < records.size(); i++) {
            boolean win = records.get(i).isWin();
            Snake.MoveResult cause = records.get(i).getCause();
            int duration = records.get(i).getDuration();
            int myLength = records.get(i).getMyLength();
            int enemyLength = records.get(i).getEnemyLength();
            if (win) {
                X++;
                if (cause.equals(Snake.MoveResult.HIT_ENEMY))
                    A++;
                else if (cause.equals(Snake.MoveResult.OUT) || cause.equals(Snake.MoveResult.SUICIDE))
                    B++;
            } else {
                Y++;
                if (cause.equals(Snake.MoveResult.HIT_ENEMY))
                    C++;
                else if (cause.equals(Snake.MoveResult.OUT) || cause.equals(Snake.MoveResult.SUICIDE))
                    D++;
            }
            T += duration;
            L1 += myLength;
            L2 += enemyLength;
        }
        data.N = DBHelper.getInstance(context).getCount();
        data.X = X;
        data.A = A;
        data.B = B;
        data.Y = Y;
        data.C = C;
        data.D = D;
        data.T = T / data.N;
        data.L1 = L1 / data.N;
        data.L2 = L2 / data.N;
        data.W = (int)Math.round((100/data.N)
                * ((7 * data.A + 5 * data.B)
                * (18 - (Math.log(data.T + 1) / Math.log(2)))
                + (data.C + 3 * data.D) * (Math.log(data.T + 2) / Math.log(2))));
        if (data.W >= 8500)
            data.P = rank[0];
        else if (data.W >= 6100)
            data.P = rank[1];
        else if (data.W >= 3800)
            data.P = rank[2];
        else if (data.W >= 1500)
            data.P = rank[3];
        else
            data.P = rank[4];
        return data;
    }
}
