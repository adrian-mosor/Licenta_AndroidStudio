package com.example.licenta;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class connectToMatrixTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {

        Log.d("debugMode", "connectToMatrixTask: do in background");

        String url = "http://192.168.10.90:80/";
        String message = "rgb_hi";

        makeHttpRequest h1 = new makeHttpRequest();
        try {
            h1.sendPostRequest(url, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Update UI here
        // For example, show a toast message indicating that the connection was successful
        Log.d("debugMode", "connectToMatrixTask: success async");
    }
}
