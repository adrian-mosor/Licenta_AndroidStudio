package com.example.licenta;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class getHeartbeatTask extends AsyncTask<Void, Void, Void> {

    private String url;
    private String heartBeat;
    private Boolean isOnline = true;

    public Context mContext;

    private TextView mStatusText;

    public getHeartbeatTask(Context context, TextView statusText, String channelId, String apiKey){

        Log.d("debugMode", "getHeartBeat task enter");

        mContext = context;
        mStatusText = statusText;
        url = "https://api.thingspeak.com/channels/" + channelId + "/fields/1.json?api_key=" + apiKey + "&results=1";
        isOnline = true;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Log.d("debugModeH", "getHeartBeat task: do in background");

        OkHttpClient client1 = new OkHttpClient();

        Request request1 = new Request.Builder()
                .url(url)
                .build();

        try{
            Response response1 = client1.newCall(request1).execute();
            String responseBody1 = response1.body().string();

            JSONObject jsonObject1 = new JSONObject(responseBody1);
            JSONArray feeds1 = jsonObject1.getJSONArray("feeds");

            if(feeds1.length() > 0 ) {
                JSONObject lastEntry1 = feeds1.getJSONObject(0);
                heartBeat = lastEntry1.getString("field1");

                // Check if the timestamp of the last heartbeat is more than 15 seconds ago
                String timestampString = lastEntry1.getString("created_at");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date timestamp = format.parse(timestampString);
                Date now = new Date();
                long secondsSinceLastHeartbeat = (now.getTime() - timestamp.getTime()) / 1000;
                long difference = now.getTime() - timestamp.getTime();

                Log.d("debugModeCH", "timestamp now: " + now.getTime());
                Log.d("debugModeCH", "timestamp thingSpeak: " + timestamp.getTime());
                Log.d("debugModeCH", "timestamp difference: " + difference);
                Log.d("debugModeCH", "secondsSinceLastHeartBeat: " + secondsSinceLastHeartbeat);

                if (secondsSinceLastHeartbeat > 7220) { //7220
                    Log.d("debugModeH", "if seconds >");
                    isOnline = false; // The ESP8266 is offline
                }else{
                    Log.d("debugModeH", "if not else");
                }
            }
        }catch(IOException | JSONException | ParseException e){

            Log.d("debugMode", "getHeartBeat task catch!");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        Log.d("debugModeH", "Heartbeat feature: success async");
        Log.d("debugModeH", "Heartbeat async post value: " + heartBeat);

        if(isOnline){
            mStatusText.setText("status online");
        }else{
            mStatusText.setText("status offline");
        }
    }
}
