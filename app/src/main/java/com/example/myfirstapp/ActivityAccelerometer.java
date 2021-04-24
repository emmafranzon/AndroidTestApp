package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;

import android.os.Bundle;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class ActivityAccelerometer extends AppCompatActivity implements SensorEventListener{
    private TextView xText, yText, zText, tiltText, accBackground, counter;
    private Sensor accSensor;
    private SensorManager sm;
    private ImageView img;
    private MediaPlayer mpLeft, mpRight, mpUpside, mpLying;
    private Button pauseMP;
    private boolean playing = false;
    //implementera LowPass - https://www.built.io/blog/applying-low-pass-filter-to-android-sensor-s-readings
    static final float ALPHA = 0.25f; // if ALPHA = 1 OR 0, no filter applies.
    private float[] accSensorVals;

    //https://developer.android.com/guide/components/activities/activity-lifecycle
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener((SensorEventListener) this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);

        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);
        tiltText = (TextView) findViewById(R.id.tiltText);
        pauseMP = (Button) findViewById(R.id.pause);
        accBackground = (TextView) findViewById(R.id.acc_background);
        img = (ImageView) findViewById(R.id.accImage);
        counter = (TextView) findViewById(R.id.counter);


        mpLeft = MediaPlayer.create(getApplicationContext(), R.raw.left);
        mpRight = MediaPlayer.create(getApplicationContext(), R.raw.right);
        mpUpside = MediaPlayer.create(getApplicationContext(), R.raw.upsidedown);
        mpLying = MediaPlayer.create(getApplicationContext(), R.raw.lying);

        pauseMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playing == false){
                    Toast.makeText(getApplicationContext(), "Tilt phone to play first", Toast.LENGTH_SHORT)
                            .show();
                }
                onResume();
                pauseMP();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accSensorVals = lowPass(event.values.clone(), accSensorVals);
        }
        //Displays lowpass vals thus these are more keen to the eye
        xText.setText("X: " + round(accSensorVals[0]));
        yText.setText("Y: " + round(accSensorVals[1]));
        zText.setText("Z: " + round(accSensorVals[2]));

        //Use lowpass vals when calling tiltchange method
        float x = Math.round(accSensorVals[0]);
        float y = Math.round(accSensorVals[1]);
        float z = Math.round(accSensorVals[2]);
        tiltChange(x, y, z);
    }

    public void tiltChange(float xVal, float yVal, float zVal){
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Handler handler = new Handler();

        if(zVal > 9) {
            accBackground.setBackgroundColor(Color.rgb(255,178,102));
            tiltText.setText("LYING");
            img.setImageResource(R.drawable.lying);
            onPause();
            mpLying.start();
            playing = true;
            //https://developer.android.com/reference/android/os/CountDownTimer.html
            new CountDownTimer(8000, 1000) {
                public void onTick(long millisUntilFinished) {
                    counter.setText("seconds remaining: " + millisUntilFinished / 1000);
                }
                public void onFinish() {
                    counter.setText("done!");
                }
            }.start();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onResume();
                    playing = false;
                }
            }, 8000);

        } else if(xVal < 4 && xVal > -4 && yVal < 0){
            accBackground.setBackgroundColor(Color.rgb(255,255,102));
            tiltText.setText("UPSIDE DOWN");
            img.setImageResource(R.drawable.upside);
            onPause();
            mpUpside.start();
            playing = true;
            new CountDownTimer(5000, 1000) {
                public void onTick(long millisUntilFinished) {
                    counter.setText("seconds remaining: " + millisUntilFinished / 1000);
                }
                public void onFinish() {
                    counter.setText("done!");
                }
            }.start();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onResume();
                    playing = false;
                }
            }, 5000);
        } else if(xVal < -5){
            accBackground.setBackgroundColor(Color.rgb(204,153,255));
            tiltText.setText("RIGHT");
            img.setImageResource(R.drawable.right);
            onPause();
            mpRight.start();
            playing = true;
            new CountDownTimer(4000, 1000) {
                public void onTick(long millisUntilFinished) {
                    counter.setText("seconds remaining: " + millisUntilFinished / 1000);
                }
                public void onFinish() {
                    counter.setText("done!");
                }
            }.start();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onResume();
                    playing = false;
                }
            }, 4000);
        } else if(xVal > 7.5){
            accBackground.setBackgroundColor(Color.rgb(102,255,102));
            tiltText.setText("LEFT");
            img.setImageResource(R.drawable.left);
            onPause();
            mpLeft.start();
            playing = true;
            new CountDownTimer(4000, 1000) {
                public void onTick(long millisUntilFinished) {
                    counter.setText("seconds remaining: " + millisUntilFinished / 1000);
                }
                public void onFinish() {
                    counter.setText("done!");
                }
            }.start();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onResume();
                    playing = false;
                }
            }, 4000);
        } else {
            if (yVal > 0) {
                accBackground.setBackgroundColor(Color.TRANSPARENT);
                tiltText.setText("STANDING");
                img.setImageResource(R.drawable.tiltphone);
                counter.setText("");
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not in use
    }

    private String round(float value){
        DecimalFormat df = new DecimalFormat("##.00");
        return df.format(value);
    }

    private void pauseMP() {
        playing = false;
        if(mpLeft.isPlaying()){
            mpLeft.pause();
        }
        if(mpRight.isPlaying()){
            mpRight.pause();
        }
        if(mpUpside.isPlaying()){
            mpUpside.pause();
        }
        if(mpLying.isPlaying()){
            mpLying.pause();
        }
    }

    protected float[] lowPass(float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Close Accelerometer?")
                .setMessage("Are you sure you don't want to listen to Beyoncé one more time?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
    }