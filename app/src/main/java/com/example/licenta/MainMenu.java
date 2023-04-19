package com.example.licenta;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

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

    private SensorDataViewModel sensorDataViewModel;

    private TextView temperatureText;
    private TextView temperatureFText;
    private TextView humidityText;
    private TextView statusText;
    private TextView espError;

    private Button enrollButton;

    private ScheduledExecutorService mExecutorService;

    boolean isESPOnline = false;    //package-private

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        sensorDataViewModel = new ViewModelProvider(this).get(SensorDataViewModel.class);

        // Observe LiveData changes
        sensorDataViewModel.getTemperature().observe(this, value -> {
            temperatureText.setText(value);
        });

        sensorDataViewModel.getTemperatureF().observe(this, value -> {
            temperatureFText.setText(value);
        });

        sensorDataViewModel.getHumidity().observe(this, value -> {
            humidityText.setText(value);
        });

        statusText = findViewById(R.id.textView_esp_status);
        enrollButton = findViewById(R.id.button_enroll);
        espError = findViewById(R.id.textview_error_esp);

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

                if(isESPOnline) {   //check the status of ESP for successfull syncing

                    espError.setVisibility(View.GONE);  //in case it was showed and due to possible delays

                    Intent intent = new Intent(MainMenu.this, SyncAccount.class);
                    startActivity(intent);
                }else{

                    espError.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void startTimer() {

        mExecutorService = Executors.newSingleThreadScheduledExecutor();

        // Schedule the RetrieveDataThingSpeakTask
        mExecutorService.scheduleAtFixedRate(() -> {
            retrieveDataThingSpeakTask task = new retrieveDataThingSpeakTask(MainMenu.this, channelID1, apiKey1, channelID2, apiKey2, channelID3, apiKey3);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }, 2, 3, TimeUnit.SECONDS);

        // Schedule the GetHeartbeatTask
        mExecutorService.scheduleAtFixedRate(() -> {
            getHeartbeatTask task = new getHeartbeatTask(MainMenu.this, statusText, channelIDHeartBeat, apiKeyHeartbeat);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }, 2, 10, TimeUnit.SECONDS);
    }


    private void stopTimer() {
        mExecutorService.shutdown();
    }

    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

}
