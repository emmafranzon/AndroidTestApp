package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Button1
        //get the button
        Button compBtn = (Button) findViewById(R.id.compassButton);
        //action when click
        compBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Calibrating Compass", Toast.LENGTH_SHORT)
                        .show();
                openCompass();
            }
        });

        //Button2
        Button accBtn = (Button) findViewById(R.id.accButton);
        accBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Calibrating Accelerometer", Toast.LENGTH_SHORT)
                        .show();

                openAccelerometer();

            }
        });
    }

    public void openCompass(){
        Intent intent = new Intent(this, ActivityCompass.class);
        startActivity(intent);
    }

    public void openAccelerometer(){
        Intent intent = new Intent(this, ActivityAccelerometer.class);
        startActivity(intent);
    }
}