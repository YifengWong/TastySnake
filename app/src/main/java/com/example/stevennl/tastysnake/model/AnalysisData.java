package com.example.stevennl.tastysnake.model;

import android.content.Context;

import com.example.stevennl.tastysnake.R;

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
        data.N = 0;
        data.X = 1;
        data.A = 2;
        data.B = 3;
        data.Y = 4;
        data.C = 5;
        data.D = 6;
        data.T = 7;
        data.L1 = 8;
        data.L2 = 9;
        data.W = 10;
        data.P = rank[0] + "/" + rank[1] + "/" + rank[2] + "/" + rank[3] + "/" + rank[4];
        return data;
    }
}
