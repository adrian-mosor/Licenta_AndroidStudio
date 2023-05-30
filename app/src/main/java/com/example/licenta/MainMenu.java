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

//    private String apiKey1 = "2EC6VJW7CNBAFMSK";
//    private String channelID1 = "2123504";
//
//    private String apiKey2 = "K5VMZMJQWIG6M68X";
//    private String channelID2 = "2123505";
//
//    private String apiKey3 = "G7J02CAXBRYS1YZL";
//    private String channelID3 = "2123506";

    private String apiKey1;
    private String channelID1;

    private String apiKey2;
    private String channelID2;

    private String apiKey3;
    private String channelID3;

    private final String apiKeyHeartbeat = "B0HP8Y8MUQIWP3T6";  //dedicated channel
    private final String channelIDHeartBeat = "2123478";

    private SensorDataViewModel mSensorDataViewModel;

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

        SharedPreferencesHelper credentialStorage = new SharedPreferencesHelper(this);

        apiKey1 = credentialStorage.getApiKey1();
        apiKey2 = credentialStorage.getApiKey2();
        apiKey3 = credentialStorage.getApiKey3();

        channelID1 = credentialStorage.getChannelId1();
        channelID2 = credentialStorage.getChannelId2();
        channelID3 = credentialStorage.getChannelId3();

        Log.d("debugStorage", "channelID1: " + channelID1);
        Log.d("debugStorage", "channelID2: " + channelID2);
        Log.d("debugStorage", "channelID3: " + channelID3);
        Log.d("debugStorage", "APIKey1: " + apiKey1);
        Log.d("debugStorage", "APIKey2: " + apiKey2);
        Log.d("debugStorage", "APIKey3: " + apiKey3);

        mSensorDataViewModel = new ViewModelProvider(this).get(SensorDataViewModel.class);

        // Observe LiveData changes
        mSensorDataViewModel.getTemperature().observe(this, value -> {
            temperatureText.setText(value);
        });

        mSensorDataViewModel.getTemperatureF().observe(this, value -> {
            temperatureFText.setText(value);
        });

        mSensorDataViewModel.getHumidity().observe(this, value -> {
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