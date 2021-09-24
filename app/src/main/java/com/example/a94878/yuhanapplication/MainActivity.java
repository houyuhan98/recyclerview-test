package com.example.a94878.yuhanapplication;

import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    static MyRecyclerViewAdapter adapter;
    static ArrayList<String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton toggle = findViewById(R.id.friendToggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Constant.areFriend = true;
                    Log.d("test", "switch is enabled");
                } else {
                    Constant.areFriend = false;
                    Log.d("test", "switch is disabled");
                }
            }
        });

        // set up the RecyclerView
        recyclerView = findViewById(R.id.rvusers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        user = new ArrayList<>();

        adapter = new MyRecyclerViewAdapter(getApplicationContext(), user);
        recyclerView.setAdapter(adapter);

        //checkNetwork network = new checkNetwork(getApplicationContext());
        //network.checkinternet();

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(new NetworkChangeReceiver(), intentFilter);

        Constant.timestamp = String.valueOf(System.currentTimeMillis());
        Log.d("Current timestamp:", Constant.timestamp);
        /*
        if(globalVar.isNetworkConnected) {
            Log.i("network","internet is good");
            //new HTTPReqTask().execute();
        }
        else{
            Log.i("network","no internet");
        }
        */
    }

    public static class HTTPReqTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("https://codechallenge.secrethouse.party/?since=" + Constant.timestamp.substring(0, 10));
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder sb = new StringBuilder();

                while ((line = rd.readLine()) != null){
                    sb.append(line);
                    Log.i("epic", Constant.count + ": " + sb.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(sb.toString());
                        // get valid json --> clear stringbuilder
                        sb.setLength(0);
                        // data to display in recyclerview
                        final String s = Constant.count + ": " + jsonObject.getJSONObject("to").getString("name") + ", " + jsonObject.getJSONObject("from").getString("name") + ", " + jsonObject.getString("timestamp");
                        boolean friend = Boolean.parseBoolean(jsonObject.getString("areFriends"));

                        String curTime = jsonObject.getString("timestamp");
                        if(Integer.parseInt(curTime.substring(0, 10)) == Integer.parseInt(Constant.timestamp.substring(0, 10)) && Integer.parseInt(curTime.substring(10)) < Integer.parseInt(Constant.timestamp.substring(10)))
                            continue;

                        Constant.timestamp = curTime;

                        Log.d("areFriends", String.valueOf(Constant.areFriend));
                        Log.d("friend", String.valueOf(friend));

                        // the only condition that won't display message
                        if (Constant.areFriend && !friend){
                            Log.d("test", "not friend will skip");
                            continue;
                        }
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                user.add(0, s);
                                adapter.setuser(user);
                            }
                        });
                        Constant.count++;

                        if(Constant.count % 2000 == 0){
                            Constant.count = 0;
                            user = new ArrayList<>();
                        }
                    } catch (JSONException jsonException){
                        jsonException.printStackTrace();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}
