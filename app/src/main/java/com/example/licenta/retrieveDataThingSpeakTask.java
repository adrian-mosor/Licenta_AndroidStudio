package com.example.licenta;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class retrieveDataThingSpeakTask extends AsyncTask<Void, Void, Void>{

    private String url1;
    private String temperatureField;

    private String url2;
    private String temperatureFField;

    private String url3;
    private String humidityField;

    public Context mContext;

    private TextView mTemperatureText;
    private TextView mTemperatureFText;
    private TextView mHumidityText;


    public retrieveDataThingSpeakTask(Context context, TextView temperatureText, TextView temperatureFText, TextView humidityText, String channelId1, String apiKey1, String channelId2, String apiKey2, String channelId3, String apiKey3) {

        mContext = context;
        mTemperatureText = temperatureText;
        mTemperatureFText = temperatureFText;
        mHumidityText = humidityText;

        this.url1 = "https://api.thingspeak.com/channels/" + channelId1 + "/fields/1.json?api_key=" + apiKey1 + "&results=1";
        this.url2 = "https://api.thingspeak.com/channels/" + channelId2 + "/fields/1.json?api_key=" + apiKey2 + "&results=1";
        this.url3 = "https://api.thingspeak.com/channels/" + channelId3 + "/fields/1.json?api_key=" + apiKey3 + "&results=1";
    }


    @Override
    protected Void doInBackground(Void... params) {

        Log.d("debugModeR", "retrieveDataThingSpeak task: do in background");

        OkHttpClient client1 = new OkHttpClient();
        OkHttpClient client2 = new OkHttpClient();
        OkHttpClient client3 = new OkHttpClient();

        Request request1 = new Request.Builder()
                .url(url1)
                .build();

        Request request2 = new Request.Builder()
                .url(url2)
                .build();

        Request request3 = new Request.Builder()
                .url(url3)
                .build();

        try {

            //temperatureC
            Response response1 = client1.newCall(request1).execute();
            String responseBody1 = response1.body().string();

            JSONObject jsonObject1 = new JSONObject(responseBody1);
            JSONArray feeds1 = jsonObject1.getJSONArray("feeds");

            if(feeds1.length() > 0 ){
                JSONObject lastEntry1 = feeds1.getJSONObject(0);
                temperatureField = lastEntry1.getString("field1");
            }

            //temperatureF
            Response response2 = client2.newCall(request2).execute();
            String responseBody2 = response2.body().string();

            JSONObject jsonObject2 = new JSONObject(responseBody2);
            JSONArray feeds2 = jsonObject2.getJSONArray("feeds");

            if(feeds2.length() > 0 ){
                JSONObject lastEntry2 = feeds2.getJSONObject(0);
                temperatureFField = lastEntry2.getString("field1");
            }

            //Humidity
            Response response3 = client3.newCall(request3).execute();
            String responseBody3 = response3.body().string();

            JSONObject jsonObject3 = new JSONObject(responseBody3);
            JSONArray feeds3 = jsonObject3.getJSONArray("feeds");

            if(feeds3.length() > 0 ){
                JSONObject lastEntry3 = feeds3.getJSONObject(0);
                humidityField = lastEntry3.getString("field1");
            }

        } catch (IOException | JSONException e) {

            Log.d("debugMode", "retrieveDataThingSpeak catch!");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Update UI here
        // For example, show a toast message indicating that the connection was successful

        Log.d("debugModeR", "retrieveDataThingSpeak task: success async");
//
//        Log.d("debugModeR", "TemperatureC: " + temperatureField);
//        Log.d("debugModeR", "TemperatureF: " + temperatureFField);
//        Log.d("debugModeR", "Humidity: " + humidityField);

        mTemperatureText.setText(temperatureField);
        mTemperatureFText.setText(temperatureFField);
        mHumidityText.setText(humidityField);
    }
}
