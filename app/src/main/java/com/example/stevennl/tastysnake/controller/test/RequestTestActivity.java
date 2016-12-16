package com.example.stevennl.tastysnake.controller.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.stevennl.tastysnake.Config;
import com.example.stevennl.tastysnake.R;
import com.example.stevennl.tastysnake.base.BaseActivity;
import com.example.stevennl.tastysnake.util.CommonUtil;
import com.example.stevennl.tastysnake.util.NetworkUtil;

import java.util.HashMap;
import java.util.Map;

public class RequestTestActivity extends BaseActivity {
    private static final String TAG = "RequestTestActivity";
    private NetworkUtil networkUtil;
    private Map<String, String> params;
    private TextView infoTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_test);
        initNetworkUtil();
        initInfoTxt();
        initGetBtn();
        initPostBtn();
        initParams();
    }

    private void initNetworkUtil() {
        networkUtil = NetworkUtil.getInstance(this);
        CommonUtil.showToast(this, NetworkUtil.isNetworkAvailable(this)
                ? "Network available" : "Network unavailable");
    }

    private void initInfoTxt() {
        infoTxt = (TextView) findViewById(R.id.req_test_infoTxt);
    }

    private void initGetBtn() {
        Button getBtn = (Button) findViewById(R.id.req_test_getBtn);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkUtil.get(Config.URL_SERVER, params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        infoTxt.append(response + "\n");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        infoTxt.append(error.toString() + "\n");
                    }
                });
            }
        });
    }

    private void initPostBtn() {
        Button postBtn = (Button) findViewById(R.id.req_test_postBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkUtil.post(Config.URL_SERVER, params, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response: " + response);
                        infoTxt.append(response + "\n");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        infoTxt.append(error.toString() + "\n");
                    }
                });
            }
        });
    }

    private void initParams() {
        params = new HashMap<>();
        params.put("w", String.valueOf(5500));
    }
}
