/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import com.danov.listener.AccelSensorListener;
import com.danov.listener.PointerListener;
import com.danov.tcp.PresentationClient;

/**
 *
 * @author x
 */
public class ActionTcpScreen extends Activity {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private AccelSensorListener listener;
    private PointerListener pointerListener;

    private IPresentClient client;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the message from the intent
        Intent intent = getIntent();
        boolean connected = intent.getBooleanExtra(ConnectScreen.CONNECTION_STATUS, false);
        if (connected) {
            client = (PresentationClient) intent.getSerializableExtra(ConnectScreen.CLIENT_OBJECT);
            client.reconnect();
            setContentView(R.layout.second);
            prepareAccelerometer(client);
            preparePointerListener(client);
            preparebuttonsLsitener();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            // Create the text view
//            TextView textView = new TextView(this);
//            textView.setTextSize(40);
//            textView.setWidth(LayoutParams.MATCH_PARENT);
//            textView.setHeight(LayoutParams.WRAP_CONTENT);
//            textView.setText("Conenction Failed!");
//            setContentView(textView);
            setContentView(R.layout.error);
        }
    }

    private void prepareAccelerometer(IPresentClient client) {
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new AccelSensorListener(client);
        if (senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            senSensorManager.registerListener(listener, senAccelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }
    
    private void preparePointerListener(IPresentClient client) {
        pointerListener = new PointerListener(client);
        View myView = findViewById(R.id.scrollableContents);
        myView.setOnTouchListener(pointerListener);
        myView.setEnabled(false);
    }

    private void preparebuttonsLsitener() {
        CheckBox satView = (CheckBox) findViewById(R.id.keyboardBox);
        satView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox view = (CheckBox) v;
                // TODO Auto-generated method stub
                if (view.isChecked()) {
                    ((Button) findViewById(R.id.nextBtn)).setEnabled(true);
                    ((Button) findViewById(R.id.prevBtn)).setEnabled(true);
                    senSensorManager.unregisterListener(listener);
                    System.out.println("Checked");
                } else {
                    ((Button) findViewById(R.id.nextBtn)).setEnabled(false);
                    ((Button) findViewById(R.id.prevBtn)).setEnabled(false);
                    senSensorManager.registerListener(listener, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    System.out.println("Un-Checked");
                }
            }
        });

        CheckBox pointerBox = (CheckBox) findViewById(R.id.pointer);
        pointerBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox view = (CheckBox) v;
                // TODO Auto-generated method stub
                if (view.isChecked()) {
                    client.sendPointerMessage();
                    enablePointer(true);
                    System.out.println("Checked");
                } else {
                    client.sendPointerMessage();
                    enablePointer(false);
                    System.out.println("Un-Checked");
                }
            }
        });

        if (senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            satView.setChecked(false);
            ((Button) findViewById(R.id.nextBtn)).setEnabled(false);
            ((Button) findViewById(R.id.prevBtn)).setEnabled(false);
        } else {
            satView.setChecked(true);
            satView.setEnabled(false);
        }
    }
    
    private void enablePointer(boolean enabled) {
        View myView = findViewById(R.id.scrollableContents);
        myView.setEnabled(enabled);
    }

    @Override
    public void onBackPressed() {
        // Otherwise defer to system default behavior.
        if(client != null){
            client.disconnect();
        }
        super.onBackPressed();
    }

    public void nextSlide(View view) {
        // Do something in response to button
        listener.next();
    }

    public void previousSlide(View view) {
        // Do something in response to button
        listener.prev();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (listener != null) {
            senSensorManager.unregisterListener(listener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listener != null) {
            senSensorManager.registerListener(listener, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
