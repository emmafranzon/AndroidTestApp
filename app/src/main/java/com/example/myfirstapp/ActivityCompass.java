package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
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

    //implementera LowPass - https://www.built.io/blog/applying-low-pass-filter-to-android-sensor-s-readings
    static final float ALPHA = 0.25f; // if ALPHA = 1 OR 0, no filter applies.
    private float[] compSensorVals;

    //Adding Media Player
    private MediaPlayer compMP;

    //https://developer.android.com/guide/components/activities/activity-lifecycle
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, compSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }


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

        compMP = MediaPlayer.create(getApplicationContext(), R.raw.beep);

    }

    @Override
    //https://www.codespeedy.com/simple-compass-code-with-android-studio/
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
            compSensorVals = lowPass(event.values.clone(), compSensorVals);
        }
        // get angle around the z-axis rotated (Azimuth directly since using orientation sensor)
        float degree = compSensorVals[0];
        compDegrees.setText("Precise heading: " + degree + " °" );
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
        compDir.setTextColor(Color.rgb(255, 255, 255));
        if(f >= 0 && f <= 22) {
            compDir.setText(round(f) + "° N");
            compDir.setTextColor(Color.rgb(255, 0, 0));
            vib.vibrate(100);
            compMP.start();

        }
        if(f > 22 && f < 67){
            compDir.setText(round(f) + "° NE");
        }
        if(f >= 67 && f <= 112){
            compDir.setText(round(f) + "° E");
        }
        if(f > 112 && f < 157){
            compDir.setText(round(f) + "° SE");
        }
        if(f >= 157 && f <= 202){
            compDir.setText(round(f) + "° S");
        }
        if(f > 202 && f < 247){
            compDir.setText(round(f) + "° SW");
        }
        if(f >= 247 && f <= 292){
            compDir.setText(round(f) + "° W");
        }
        if(f > 292 && f < 314){
            compDir.setText(round(f) + "° NW");
        }
        if(f >= 314 && f <= 360){
            compDir.setText(round(f) + "° N");
            compDir.setTextColor(Color.rgb(255, 0, 0));
            vib.vibrate(100);
            compMP.start();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    protected float[] lowPass( float[] input, float[] output ) {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    //Rounding off heading by 2 decimals
    private String round(float value){
        DecimalFormat df = new DecimalFormat("##.00");
        return df.format(value);
    }




}