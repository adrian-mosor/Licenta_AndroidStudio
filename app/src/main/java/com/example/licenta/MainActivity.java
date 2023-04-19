package com.example.licenta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity{

    private Button goToMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferencesHelper credentialStorage = new SharedPreferencesHelper(this);

        goToMainMenu = findViewById(R.id.button_connect);
        goToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("debugMode", "onClickConnectToMatrix");

                credentialStorage.setApiKey1("486R5S7M0349NF3V");
                credentialStorage.setApiKey2("NVON0G6E6POWKGRP");
                credentialStorage.setApiKey3("1ZCLKDX2REMRH31O");

                credentialStorage.setChannelId1("2039388");
                credentialStorage.setChannelId2("2071204");
                credentialStorage.setChannelId3("2071205");

                connectToMatrixTask task = new connectToMatrixTask();
                task.execute();

                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });

    }

}
