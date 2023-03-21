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

        goToMainMenu = findViewById(R.id.button_connect);
        goToMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  Log.d("debugMode", "onClickConnectToMatrix");
//                  connectToMatrixTask task = new connectToMatrixTask();
//                  task.execute();


                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
            }
        });

    }

}
