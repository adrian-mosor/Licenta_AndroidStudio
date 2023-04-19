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

import okhttp3.OkHttpClient;
import okhttp3.Request;
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

        apiKey1.setFilters(new InputFilter[]{apiKeyFilter, new InputFilter.LengthFilter(16)});
        apiKey2.setFilters(new InputFilter[]{apiKeyFilter, new InputFilter.LengthFilter(16)});
        apiKey3.setFilters(new InputFilter[]{apiKeyFilter, new InputFilter.LengthFilter(16)});

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

                        successMessage.setText("Successful Sync!");
                        successMessage.setVisibility(View.VISIBLE);

                        String url = "http://" + ipAddress + ":80/";
                        String message = "thingspeak_sync";

                        try {
                            makeHttpRequest.sendPostRequest(url, message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {  //in case we can't get the IP stored in dweet (this would be a rare failure)

                        errorMessage.setText("There was a problem with the IP address reported by ESP! Please try a reboot.");
                        errorMessage.setVisibility(View.VISIBLE);

                        //delay of going back to MainMenu for the user to see the errorMessage
                        int delayMillis = 2000; // The delay in milliseconds (e.g., 2000 ms = 2 seconds)
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SyncAccount.this, MainMenu.class);
                                startActivity(intent);
                            }
                        }, delayMillis);

                    }

                }   //stop of success else
            }
        });
    }

    public boolean isAllUpperCaseOrDigit(String input) {
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
