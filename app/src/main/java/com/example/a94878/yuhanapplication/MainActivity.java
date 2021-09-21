package com.example.a94878.yuhanapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    ArrayList<String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up the RecyclerView
        recyclerView = findViewById(R.id.rvusers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        user = new ArrayList<>();

        new HTTPReqTask().execute();
    }

    private class HTTPReqTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            try {
                String unixTime = String.valueOf(System.currentTimeMillis() / 1000L);
                URL url = new URL("https://codechallenge.secrethouse.party/?since=" + unixTime);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(in));
                String line;
                int count=0;
                while ((line = rd.readLine()) != null && count<2000) {
                    Log.i("epic", line);
                    user.add(0,line);
                    count++;
                }
                adapter = new MyRecyclerViewAdapter(getApplicationContext(), user);
                recyclerView.setAdapter(adapter);
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
