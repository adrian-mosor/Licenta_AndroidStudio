package com.example.licenta;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class connectToMatrixTask extends AsyncTask<Void, Void, Void> {

    private static final String DWEET_BASE_URL = "https://dweet.io/get/latest/dweet/for/";

    @Override
    protected Void doInBackground(Void... params) {

        Log.d("debugModeC", "connectToMatrixTask: do in background");

        String esp8266_id = "smart_matrix_esp8266";
        String ipAddress = getIPAddressFromDweet(esp8266_id);
        Log.d("debugModeC", "IP of ESP8266 is: " + ipAddress);

        if (ipAddress != null) {
            String url = "http://" + ipAddress + ":80/";
            String message = "rgb_hi";

            makeHttpRequest h1 = new makeHttpRequest();
            try {
                h1.sendPostRequest(url, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("debugModeC", "connectToMatrixTask: IP address not found");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Update UI here
        // For example, show a toast message indicating that the connection was successful
        Log.d("debugModeC", "connectToMatrixTask: success async");
    }

    private String getIPAddressFromDweet(String esp8266_id) {
        String ipAddress = null;
        String url = DWEET_BASE_URL + esp8266_id;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONArray jsonArray = jsonObject.getJSONArray("with");
                    JSONObject latestDweet = jsonArray.getJSONObject(0);
                    JSONObject content = latestDweet.getJSONObject("content");
                    ipAddress = content.getString("ip");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ipAddress;
    }

}
