package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.os.Bundle;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class ActivityCompass extends Activity implements SensorEventListener {

    // define the display assembly compass picture
    private ImageView compassImage;

    // device sensor manager
    private SensorManager sm;
    private Sensor compSensor;

    //TextView for degrees
    private TextView compDegrees, compDir, compMess;

    // record the angle turned of the compass picture
    private float DegreeStart = 0f;

    //implementera LowPass - https://www.built.io/blog/applying-low-pass-filter-to-android-sensor-s-readings
    static final float ALPHA = 0.25f; // if ALPHA = 1 OR 0, no filter applies.
    private float[] compSensorVals;

    //Adding Media Player
    //https://stackoverflow.com/questions/10849961/speed-control-of-mediaplayer-in-android
    private MediaPlayer compMP;
    private float speedClose = 1.5f;
    private float speed = 0.75f;

    //Progressbar
    private ProgressBar pb;
    private int pbStatus = 0;
    private TextView procenatge;


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

        compDegrees = (TextView) findViewById(R.id.compass_location);
        compDir = (TextView) findViewById(R.id.compass_direction);
        compassImage = (ImageView) findViewById(R.id.compass_image);
        //ADDED SENSOR AND LISTENER
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        compSensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sm.registerListener((SensorEventListener) this, compSensor, SensorManager.SENSOR_DELAY_NORMAL);

        compMP = MediaPlayer.create(getApplicationContext(), R.raw.beep);
        compMess = (TextView) findViewById(R.id.compass_message);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        procenatge = (TextView) findViewById(R.id.procentage);

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
        compMess.setText("Go as north as you can!");
        procenatge.setText(pbStatus + " %");
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

    @SuppressLint("NewApi")
    public void degreeChange(Float f){
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        compDir.setTextColor(Color.rgb(255, 255, 255));
        if(f <12 || f > 348){
            compDir.setText(round(f) + "° N");
            compDir.setTextColor(Color.rgb(255, 0, 0));
            vib.vibrate(100);
            compMP.setPlaybackParams(compMP.getPlaybackParams().setSpeed(speedClose));
            compMP.start();
            pbStatus++;
            pb.setProgress(pbStatus);
            procenatge.setText(pbStatus + " %");
            headingNorth(pbStatus);

        }
        if(f >= 12 && f <= 22) {
            compDir.setText(round(f) + "° N");
            compDir.setTextColor(Color.rgb(255, 0, 0));
            vib.vibrate(10);
            compMP.setPlaybackParams(compMP.getPlaybackParams().setSpeed(speed));
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
        if(f >= 314 && f <= 348){
            compDir.setText(round(f) + "° N");
            compDir.setTextColor(Color.rgb(255, 0, 0));
            vib.vibrate(10);
            compMP.setPlaybackParams(compMP.getPlaybackParams().setSpeed(speed));
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

    private void headingNorth(int x){
        if(x >= 100){
            compMess.setText("WIHO!");
            sm.unregisterListener(this);
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_trophy)
                    .setTitle("YOU WIN")
                    .setMessage("You are actually so cool!")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .show();

        }
        if(x < 25){
            compMess.setText("You can do better!");
            compMess.setTextColor(Color.rgb(255, 0, 0));
            procenatge.setTextColor(Color.rgb(255, 0, 0));
        }
        if(x >= 25 && x <50){
            compMess.setText("Keep on going!!");
            compMess.setTextColor(Color.rgb(255, 128, 0));
            procenatge.setTextColor(Color.rgb(255, 128, 0));
        }
        if(x >= 50 && x < 75){
            compMess.setText("Halfway to Victory!");
            compMess.setTextColor(Color.rgb(255, 255, 0));
            procenatge.setTextColor(Color.rgb(255, 255, 0));
        }
        if(x >= 75 && x < 90) {
            compMess.setText("It is getting colder!!");
            compMess.setTextColor(Color.rgb(0, 255, 255));
            procenatge.setTextColor(Color.rgb(0, 255, 255));
        }
        if (x >= 90 && x < 100){
            compMess.setText("You are SO close!");
            compMess.setTextColor(Color.rgb(0, 255, 0));
            procenatge.setTextColor(Color.rgb(0, 255, 0));
        }
    }

    //https://stackoverflow.com/questions/2257963/how-to-show-a-dialog-to-confirm-that-the-user-wishes-to-exit-an-android-activity
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Close Compass?")
                .setMessage("Are you sure you want to close? You will loose all your coolness")
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