package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;

import android.os.Bundle;

import java.text.DecimalFormat;

public class ActivityAccelerometer extends AppCompatActivity implements SensorEventListener{

    private TextView xText, yText, zText, tiltText, accBackground;
    private Sensor accSensor;
    private SensorManager sm;
    private ImageView img;

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
        accBackground = (TextView) findViewById(R.id.acc_background);
        img = (ImageView) findViewById(R.id.accImage);

    }

    private String round(float value){
        DecimalFormat df = new DecimalFormat("##.00");
        return df.format(value);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        xText.setText("X: " + round(event.values[0]));
        yText.setText("Y: " + round(event.values[1]));
        zText.setText("Z: " + round(event.values[2]));

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        tiltChange(x, y, z);
    }

    public void tiltChange(float xVal, float yVal, float zVal){
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(zVal > 8) {
            accBackground.setBackgroundColor(Color.rgb(255,178,102));
            tiltText.setText("LYING");
            img.setImageResource(R.drawable.lying);
        } else if(xVal < 4 && xVal > -4) {
            if(yVal > 0){
                accBackground.setBackgroundColor(Color.TRANSPARENT);
                tiltText.setText("STANDING");
                img.setImageResource(R.drawable.ic_launcher_foreground);
            }
            if(yVal < 0) {
                accBackground.setBackgroundColor(Color.rgb(255,255,102));
                tiltText.setText("UPSIDE DOWN");
                img.setImageResource(R.drawable.upside);
            }
        } else if(xVal < -5){
            accBackground.setBackgroundColor(Color.rgb(204,153,255));
            tiltText.setText("RIGHT");
            img.setImageResource(R.drawable.right);
        } else if(xVal > 7.5){
            accBackground.setBackgroundColor(Color.rgb(102,255,102));
            tiltText.setText("LEFT");
            img.setImageResource(R.drawable.left);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not in use
    }
}