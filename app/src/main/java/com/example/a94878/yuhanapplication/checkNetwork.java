package com.example.a94878.yuhanapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class checkNetwork {
    Context context;

    public checkNetwork(Context context) {
        this.context = context;
    }

    // Network Check
    public void checkinternet()
    {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            Network[] networks = cm.getAllNetworks();
            for (Network n: networks) {
                NetworkInfo nInfo = cm.getNetworkInfo(n);
                if (nInfo != null && nInfo.isConnected()) globalVar.isNetworkConnected = true;
            }
        }catch (Exception e){
            globalVar.isNetworkConnected = false;
        }
    }
}
