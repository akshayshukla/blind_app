package com.major.project.drishti;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private int count = 0;
    private long startMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ONCREATE", "STARTED");

        checkAppRunCount();

        setContentView(R.layout.activity_main);
        RelativeLayout main = findViewById(R.id.rl1);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                Log.d("SHAKE", count + "");
                if (count >= 3) {
                    mSensorManager.unregisterListener(mShakeDetector);
                    finish();
                }
            }
        });


        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST", "ONCLICK");
                long time = System.currentTimeMillis();
                if (startMillis == 0 || (time - startMillis > 2000)) {
                    checkTapCount();
                    startMillis = time;
                    count = 1;
                } else {
                    count++;
                }
            }
        });
        checkTapCount();
    }


    @Override
    public void onResume() {
        Log.d("onResume", "STARTED");
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        Log.d("onPause", "STARTED");
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);

        super.onPause();
    }

    @Override
    protected void onStart() {
        Log.d("onStart", "STARTED");
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        super.onStart();
    }

    @Override
    protected void onStop() {
//        Log.d("onStop", "STARTED");
        mSensorManager.unregisterListener(mShakeDetector);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("onDestroy", "STARTED");
        mSensorManager.unregisterListener(mShakeDetector);
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
//        Log.d("onRestart", "STARTED");
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        super.onRestart();
    }

    private void checkTapCount() {
        Log.d("TOUCH", count + "");
        if (count == 5) {
            SharedPreferences sharedPreferences
                    = getBaseContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            String mobile = sharedPreferences.getString("Mobile", "0");
            String message = sharedPreferences.getString("Message", "");
            String dial = "tel:" + mobile;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                return;
            }
            Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + mobile));
            smsIntent.putExtra("sms_body", message);
            startActivity(smsIntent);
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));


        }else if(count == 4){

            Intent i1 = new Intent();
            i1.setClass(getApplicationContext(),GoogleMaps.class);
            startActivity(i1);

        }else if (count == 3){

            Intent i1 = new Intent();
            i1.setClass(getApplicationContext(),ObjectRecognition.class);
            startActivity(i1);

        }else if (count == 2){
            Toast.makeText(getApplicationContext(),"TAPPED 2 TIMES",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAppRunCount() {
        SharedPreferences sharedPreferences
                = getBaseContext().getSharedPreferences("MySharedPref",Context.MODE_PRIVATE);
        boolean IsFirstRun = sharedPreferences.getBoolean("IsFirstRun",true);
        if(IsFirstRun)
        {
            Intent i1 = new Intent();
            i1.setClass(getApplicationContext(),EmergencyDialer.class);
            startActivity(i1);
        }

    }
}
