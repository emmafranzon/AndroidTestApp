package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Vibrator;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import android.os.Bundle;

import java.text.DecimalFormat;

public class ActivityCompass extends Activity implements SensorEventListener {

    // define the display assembly compass picture
    private ImageView compassImage;

    // device sensor manager
    private SensorManager sm;
    private Sensor compSensor;

    //TextView for degrees
    private TextView compDegrees, compDir;

    // record the angle turned of the compass picture
    private float DegreeStart = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        compDegrees = (TextView) findViewById(R.id.compass_degrees);
        compDir = (TextView) findViewById(R.id.compass_direction);
        compassImage = (ImageView) findViewById(R.id.compass_image);
        //ADDED SENSOR AND LISTENER
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        compSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sm.registerListener((SensorEventListener) this, compSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
    //Rounding off heading by 2 decimals
    private String round(float value){
        DecimalFormat df = new DecimalFormat("##.00");
        return df.format(value);
    }

    @Override
    //https://www.codespeedy.com/simple-compass-code-with-android-studio/
    public void onSensorChanged(SensorEvent event) {
        // get angle around the z-axis rotated
        float degree = event.values[0];
        compDegrees.setText("Heading: " + round(degree) + " degrees" );
        // rotation animation - reverse turn degree degrees

        RotateAnimation ra = new RotateAnimation(
                DegreeStart,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        // set the compass animation after the end of the reservation status
        ra.setFillAfter(true);
        // set how long the animation for the compass image will take place
        ra.setDuration(210);
        // Start animation of compass image
        compassImage.startAnimation(ra);
        DegreeStart = -degree;
        degreeChange(degree);
    }

    public void degreeChange(Float f){
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(f >= 0 && f <= 22) {
            compDir.setText("N");
        }
        if(f > 22 && f < 67){
            compDir.setText("NE");
        }
        if(f >= 67 && f <= 112){
            compDir.setText("E");
        }
        if(f > 112 && f < 157){
            compDir.setText("SE");
        }
        if(f >= 157 && f <= 202){
            compDir.setText("S");
        }
        if(f > 202 && f < 247){
            compDir.setText("SW");
        }
        if(f >= 247 && f <= 292){
            compDir.setText("W");
        }
        if(f > 292 && f < 227){
            compDir.setText("NW");
        }
        if(f >= 227 && f <= 360){
            compDir.setText("N");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}