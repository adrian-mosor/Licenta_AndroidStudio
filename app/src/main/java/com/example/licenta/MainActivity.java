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

//                hardcoding credentials
//                credentialStorage.setApiKey1("2EC6VJW7CNBAFMSK");
//                credentialStorage.setApiKey2("K5VMZMJQWIG6M68X");
//                credentialStorage.setApiKey3("G7J02CAXBRYS1YZL");
//
//                credentialStorage.setChannelId1("2123504");
//                credentialStorage.setChannelId2("2123505");
//                credentialStorage.setChannelId3("2123506");

                connectToMatrixTask task = new connectToMatrixTask();
                task.execute();

                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });

    }

}