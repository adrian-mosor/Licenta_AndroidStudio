package com.example.licenta;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class makeHttpRequest {

        public static void sendPostRequest(String urlStr, String message) throws IOException {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(message);
            writer.flush();

            int responseCode = conn.getResponseCode();
//            System.out.println("Response Code : " + responseCode);
            Log.d("debugMode", "Response Code : " + responseCode);
        }

    public static void main(String[] args) throws IOException {

        String url = "http://192.168.10.121:80/";
        String message = "rgb_hi";
        sendPostRequest(url, message);
    }
}
