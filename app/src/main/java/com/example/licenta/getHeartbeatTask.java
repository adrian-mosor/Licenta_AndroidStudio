package com.example.licenta;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class getHeartbeatTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private Boolean isOnline = true;

    public Context mContext;

    private TextView mStatusText;

    public getHeartbeatTask(Context context, TextView statusText, String channelId, String apiKey) {

        mContext = context;
        mStatusText = statusText;
        url = "https://api.thingspeak.com/channels/" + channelId + "/feeds/last.json?api_key=" + apiKey;
        isOnline = true;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.d("debugModeCH", "getHeartbeat task: do in background");

        OkHttpClient client1 = new OkHttpClient();

        Request request1 = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response1 = client1.newCall(request1).execute();
            String responseBody1 = response1.body().string();
            Gson gson = new Gson();
            ThingSpeakResponse thingSpeakResponse = gson.fromJson(responseBody1, ThingSpeakResponse.class);

            // Check if the timestamp of the last heartbeat is more than 15 seconds ago
            String timestampString = thingSpeakResponse.getCreatedAt();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date timestamp = format.parse(timestampString);
            Date now = new Date();

            long secondsSinceLastHeartbeat = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - timestamp.getTime());

            Log.d("debugModeCH", "timestamp difference: " + secondsSinceLastHeartbeat);

            if (secondsSinceLastHeartbeat > 18) {
                isOnline = false; // The ESP8266 is offline
            }
        } catch (IOException | ParseException e) {

            Log.d("debugMode", "getHeartbeat catch!");
            e.printStackTrace();
        }

        return null;
    }



    @Override
    protected void onPostExecute(Void result) {

        Log.d("debugModeCH", "getHeartbeatTask task: success async");

        if (isOnline) {
            mStatusText.setText("status online");
        } else {
            mStatusText.setText("status offline");
        }
    }
}
