package com.example.stevennl.tastysnake.ui.game;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.bluetooth.BluetoothManager;

/**
 * Home page.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private GameActivity act;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        act = (GameActivity)context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        initStartBtn(v);
        return v;
    }

    private void initStartBtn(View v) {
        Button btn = (Button) v.findViewById(R.id.home_start_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BluetoothManager.getInstance().isSupport()) {
                    act.replaceFragment(new ConnectFragment(), true);
                } else {
                    CommonUtil.showToast(act, getString(R.string.bluetooth_not_support));
                }
            }
        });
    }
}
