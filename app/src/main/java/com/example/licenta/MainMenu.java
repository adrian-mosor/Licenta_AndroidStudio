package com.example.licenta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainMenu extends AppCompatActivity {

    private final String apiKey1 = "486R5S7M0349NF3V";
    private final String channelID1 = "2039388";

    private final String apiKey2 = "NVON0G6E6POWKGRP";
    private final String channelID2 = "2071204";

    private final String apiKey3 = "1ZCLKDX2REMRH31O";
    private final String channelID3 = "2071205";

    private final String apiKeyHeartbeat = "4F94OIT6QBHHTRRB";
    private final String channelIDHeartBeat = "2074976";

    private TextView temperatureText;
    private TextView temperatureFText;
    private TextView humidityText;
    private TextView statusText;

    private Button enrollButton;

    private ScheduledExecutorService mExecutorService;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        statusText = findViewById(R.id.textView_esp_status);
        enrollButton = findViewById(R.id.button_enroll);

        startTimer();

        //passed to AsyncTask when retrieving data
        temperatureText = findViewById(R.id.textView_temperature);
        temperatureFText = findViewById(R.id.textView_temperatureF);
        humidityText = findViewById(R.id.textView_humidity);

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("debugModeM", "pushed button");

                connectToMatrixTask task = new connectToMatrixTask();
                task.execute();

                Intent intent = new Intent(MainMenu.this, SyncAccount.class);
                startActivity(intent);
            }
        });
    }

    private void startTimer() {

        mExecutorService = Executors.newScheduledThreadPool(2);
        mExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {

                retrieveDataThingSpeakTask task = new retrieveDataThingSpeakTask(MainMenu.this, temperatureText, temperatureFText, humidityText, channelID1, apiKey1, channelID2, apiKey2, channelID3, apiKey3);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                getHeartbeatTask task1 = new getHeartbeatTask(MainMenu.this, statusText, channelIDHeartBeat, apiKeyHeartbeat);
                task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, 1, 2, TimeUnit.SECONDS);

    }

    private void stopTimer() {
        mExecutorService.shutdown();
    }

    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

}
