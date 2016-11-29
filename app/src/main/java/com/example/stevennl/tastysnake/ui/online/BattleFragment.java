package com.example.stevennl.tastysnake.ui.online;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stevennl.tastysnake.R;

public class BattleFragment extends Fragment {
    private static final String TAG = "BattleFragment";
    private OnlineGameActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (OnlineGameActivity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_battle, container, false);

        return v;
    }
}
