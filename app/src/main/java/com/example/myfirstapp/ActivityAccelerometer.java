package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Vibrator;
import android.widget.TextView;

import android.os.Bundle;

import java.text.DecimalFormat;

public class ActivityAccelerometer extends AppCompatActivity implements SensorEventListener{

    private TextView xText, yText, zText, tiltText;
    private Sensor accSensor;
    private SensorManager sm;

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
        tiltChange(x, y);
    }

    public void tiltChange(float xVal, float yVal){
        if(xVal < 4 && xVal > -4) {
            if(yVal > 0){
                tiltText.setText("STANDING");
            }
            if(yVal < 0) {
                tiltText.setText("UPSIDE DOWN");
            }
        } else if(xVal < -5){
            tiltText.setText("RIGHT");
        } else if(xVal > 7.5){
            tiltText.setText("LEFT");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not in use
    }
}