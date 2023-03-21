package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends AppCompatActivity {

    private final String apiKey1 = "486R5S7M0349NF3V";
    private final String channelID1 = "2039388";

    private final String apiKey2 = "NVON0G6E6POWKGRP";
    private final String channelID2 = "2071204";

    private final String apiKey3 = "1ZCLKDX2REMRH31O";
    private final String channelID3 = "2071205";

    private TextView temperatureText;
    private TextView temperatureFText;
    private TextView humidityText;

    private Timer mTimer;
    private Handler mHandler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        startTimer();

        temperatureText = findViewById(R.id.textView_temperature);
        temperatureFText = findViewById(R.id.textView_temperatureF);
        humidityText = findViewById(R.id.textView_humidity);

    }

    private void startTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        retrieveDataThingSpeakTask task = new retrieveDataThingSpeakTask(MainMenu.this, temperatureText, temperatureFText, humidityText, channelID1, apiKey1, channelID2, apiKey2, channelID3, apiKey3);
                        task.execute();
                    }
                });
            }
        }, 0, 1000);

    }

    private void stopTimer() {
        mTimer.cancel();
        mTimer.purge();
    }

    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

}
