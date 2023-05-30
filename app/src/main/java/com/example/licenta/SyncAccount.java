package com.example.licenta;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SyncAccount extends AppCompatActivity {

    private static final String DWEET_BASE_URL = "https://dweet.io/get/latest/dweet/for/";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync_account);

        SharedPreferencesHelper credentialStorage = new SharedPreferencesHelper(this);

        Button syncButton = findViewById(R.id.button_sync);

        EditText apiKey1 = findViewById(R.id.api_key_1);
        EditText apiKey2 = findViewById(R.id.api_key_2);
        EditText apiKey3 = findViewById(R.id.api_key_3);
        EditText chID1 = findViewById(R.id.ch_id_1);
        EditText chID2 = findViewById(R.id.ch_id_2);
        EditText chID3 = findViewById(R.id.ch_id_3);
        TextView errorMessage = findViewById(R.id.textview_error);
        TextView successMessage = findViewById(R.id.textview_successmessage);
        Button returnButton = findViewById(R.id.button_return);

        InputFilter apiKeyFilter = new InputFilter() {  //input filter for alpha-numeric chars for API key
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);
                    if (!Character.isLetterOrDigit(currentChar)) {
                        return "";
                    }
                }
                return null;
            }
        };

        InputFilter channelIDFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dstart == 0 && source.length() > 0 && source.charAt(start) == '0') {
                    return "";
                }
                return null;
            }
        };


        //alpha-numeric chars for API Keys
        apiKey1.setFilters(new InputFilter[]{apiKeyFilter, new InputFilter.LengthFilter(16)});
        apiKey2.setFilters(new InputFilter[]{apiKeyFilter, new InputFilter.LengthFilter(16)});
        apiKey3.setFilters(new InputFilter[]{apiKeyFilter, new InputFilter.LengthFilter(16)});

        //stop channelIDs to start with zero (limitation of ESP method to store credentials)
        chID1.setFilters(new InputFilter[]{channelIDFilter, new InputFilter.LengthFilter(7)});
        chID2.setFilters(new InputFilter[]{channelIDFilter, new InputFilter.LengthFilter(7)});
        chID3.setFilters(new InputFilter[]{channelIDFilter, new InputFilter.LengthFilter(7)});

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SyncAccount.this, MainMenu.class);
                startActivity(intent);
            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputApiKey1 = apiKey1.getText().toString();
                String inputApiKey2 = apiKey2.getText().toString();
                String inputApiKey3 = apiKey3.getText().toString();
                String inputChID1 = chID1.getText().toString();
                String inputChID2 = chID2.getText().toString();
                String inputChID3 = chID3.getText().toString();


                if (inputApiKey1.length() != 16 || inputApiKey2.length() != 16 || inputApiKey3.length() != 16
                        || inputChID1.length() != 7 || inputChID2.length() != 7 || inputChID3.length() != 7
                        || !isAllUpperCaseOrDigit(inputApiKey1) || !isAllUpperCaseOrDigit(inputApiKey2) || !isAllUpperCaseOrDigit(inputApiKey3)
                ) {
                    Log.d("debugS: ", ":" + inputApiKey1.length() + inputApiKey2.length() + inputApiKey3.length() + inputChID1.length() +
                            inputChID2.length() + inputChID3.length());
                    Log.d("debugS:", ":" + StringUtils.isAllUpperCase(inputApiKey1) + StringUtils.isAllUpperCase(inputApiKey2) +
                            StringUtils.isAllUpperCase(inputApiKey3));

                    errorMessage.setText("Please add valid credentials!");
                    errorMessage.setVisibility(View.VISIBLE);

                }   //beginning of success else
                else {
                    errorMessage.setVisibility(View.GONE);  //in case it was showed eariler

                    //make http request directly to ESP IP
                    String esp8266_id = "smart_matrix_esp8266";
                    String ipAddress = getIPAddressFromDweet(esp8266_id);

                    if (ipAddress != null) {

                        Log.d("debugESP", "entered IP NOT null if");

                        String url = "http://" + ipAddress + ":80/";
                        String message = "thingspeak_sync/" + inputChID1 + "/" + inputChID2 + "/" + inputChID3 + "/" + inputApiKey1 + "/" +
                                inputApiKey2 + "/" + inputApiKey3;

                        Log.d("debugESP", "url of IPAdress with port: " + url);
                        Log.d("debugESP", "format message is " + message);

                        credentialStorage.setApiKey1(inputApiKey1);
                        credentialStorage.setApiKey2(inputApiKey2);
                        credentialStorage.setApiKey3(inputApiKey3);

                        credentialStorage.setChannelId1(inputChID1);
                        credentialStorage.setChannelId2(inputChID2);
                        credentialStorage.setChannelId3(inputChID3);

                        final MediaType TEXT_PLAIN = MediaType.parse("text/plain; charset=utf-8");
                        final boolean[] requestSuccessful = new boolean[1];
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                OkHttpClient client = new OkHttpClient();
                                RequestBody requestBody = RequestBody.create(TEXT_PLAIN, message);
                                Request request = new Request.Builder()
                                        .url(url)
                                        .post(requestBody)
                                        .build();

                                try {
                                    Response response = client.newCall(request).execute();
                                    requestSuccessful[0] = response.isSuccessful();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        thread.start();

                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (requestSuccessful[0]) {
                            successMessage.setText("Successful Sync!");
                            successMessage.setVisibility(View.VISIBLE);

                            delayGoBackToMainMenu();

                        } else {
                            errorMessage.setText("The device ESP8266 is offline. Please check the connection and try again.");
                            errorMessage.setVisibility(View.VISIBLE);
                        }



                    } else {  //in case we can't get the IP stored in dweet (this would be a rare failure)

                        errorMessage.setText("There was a problem with the IP address reported by ESP! Please try a reboot.");
                        errorMessage.setVisibility(View.VISIBLE);

                        delayGoBackToMainMenu();
                    }

                }   //stop of success else
            }
        });
    }

    private boolean isAllUpperCaseOrDigit(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c) && !Character.isUpperCase(c)) {
                return false;
            } else if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private String getIPAddressFromDweet(String esp8266_id) {
        final String[] ipAddress = new String[1];
        String url = DWEET_BASE_URL + esp8266_id;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            JSONArray jsonArray = jsonObject.getJSONArray("with");
                            JSONObject latestDweet = jsonArray.getJSONObject(0);
                            JSONObject content = latestDweet.getJSONObject("content");
                            ipAddress[0] = content.getString("ip");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ipAddress[0];
    }


    public void delayGoBackToMainMenu(){

        //delay of going back to MainMenu for the user to see the errorMessage
        int delayMillis = 3400; // The delay in milliseconds (e.g., 2000 ms = 2 seconds)
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SyncAccount.this, MainMenu.class);
                startActivity(intent);
            }
        }, delayMillis);
    }
}