/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.danov.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import com.danov.IPresentClient;
import com.danov.udp.PresentationUDPClient;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author x
 */
public class AccelSensorListener implements SensorEventListener {

    private static final int X = 0;

    private static final long TIME_THRESHOLD = 1500;
    private static final int MOVE_THRESHOLD = 5;
    private static final float alpha = 0.8f;
    
    private static final int DECIMAL_POINTS = 2;

    double[] gravity;
    double[] linearAcceleration;
    private long lastUpdate;
    private IPresentClient client;

    public AccelSensorListener() {
        this(null);
    }

    public AccelSensorListener(IPresentClient client) {
        this.client = client;
        this.gravity = new double[3];
        this.linearAcceleration = new double[3];
        this.lastUpdate = 0;
    }

    public void next() {
        client.sendNextMessage();
    }

    public void prev() {
        client.sendPrevMessage();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // Isolate the force of gravity with the low-pass filter.
            gravity[X] = round(alpha * gravity[X] + (1 - alpha) * sensorEvent.values[X],DECIMAL_POINTS);

            // Remove the gravity contribution with the high-pass filter.
            linearAcceleration[X] = round(sensorEvent.values[X] - gravity[X],DECIMAL_POINTS);

            double xChange = round(0 - linearAcceleration[X],DECIMAL_POINTS);

            long currTime = System.currentTimeMillis();
            checkX(currTime, xChange);
        }
    }

    private void checkX(long currTime, double xChange) {
        if ((currTime - lastUpdate) > TIME_THRESHOLD) {
            if ((Math.abs(linearAcceleration[X]) > 1.5)) {
                if (xChange > MOVE_THRESHOLD) {
                    prev();
                    lastUpdate = currTime;
                } else if (xChange < -MOVE_THRESHOLD) {
                    next();
                    lastUpdate = currTime;
                }
            }
        }
    }

    private double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    
    public IPresentClient getClient() {
        return client;
    }
}
