package com.example.a94878.yuhanapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("app","Network connectivity change");
        if(intent.getExtras() != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                Log.d("app","Network " + ni.getTypeName() + " connected");
                new MainActivity.HTTPReqTask().execute();
            }
        }
        if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            Log.d("app","There's no network connectivity");
            Log.d("app", "Last timestamp: " + Constant.timestamp);
        }
    }
}