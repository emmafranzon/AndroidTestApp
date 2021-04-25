package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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



    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = (TextView) findViewById(R.id.loading);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);
        loadimg = (ImageView) findViewById(R.id.loadingImg);

        RotateAnimation ra = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        ra.setInterpolator(new LinearInterpolator());
        ra.setDuration(1200);
        ra.setRepeatCount(Animation.INFINITE);

        loadimg.startAnimation(ra);

        new CountDownTimer(3000, 17) {
            public void onTick(long millisUntilFinished) {
                pbStatus++;
                progressbar.setProgress(pbStatus);
                loading.setText("Building...");
            }
            public void onFinish() {
                loading.setText("done!");
            }
        }.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivityStart();

            }
        }, 3000);

    }

    public void openActivityStart(){
        Intent intent = new Intent(this, ActivityStart.class);
        startActivity(intent);
    }

     */
}