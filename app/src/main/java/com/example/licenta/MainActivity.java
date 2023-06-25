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

                credentialStorage.setApiKey1("VSEWV1Q4Z2P6WVL2");
                credentialStorage.setApiKey2("HAAELS3S8TSE9U70");
                credentialStorage.setApiKey3("1UV47F50WM5HU7A7");

                credentialStorage.setChannelId1("2125189");
                credentialStorage.setChannelId2("2125190");
                credentialStorage.setChannelId3("2125191");

                connectToMatrixTask task = new connectToMatrixTask();
                task.execute();

                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });

    }

}