package com.example.stevennl.tastysnake.ui.online;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.stevennl.tastysnake.R;

public class ConnectFragment extends Fragment {
    private static final String TAG = "ConnectFragment";
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
        View v = inflater.inflate(R.layout.fragment_connect, container, false);
        Button btn = (Button) v.findViewById(R.id.connect_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.replaceFragment(new BattleFragment());
            }
        });
        return v;
    }
}
