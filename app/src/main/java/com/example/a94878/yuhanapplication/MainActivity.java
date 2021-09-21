package com.example.a94878.yuhanapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpURLConnection urlConnection = null;
        try {
            String unixTime = String.valueOf(System.currentTimeMillis() / 1000L);
            URL url = new URL("https://codechallenge.secrethouse.party/?since=" + unixTime);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                Log.i("epic",String.valueOf(current));
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

        ArrayList<String> user = new ArrayList<>();
        user.add("yuhan");
        user.add("chenyang");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvusers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, user);
        recyclerView.setAdapter(adapter);
    }
}
