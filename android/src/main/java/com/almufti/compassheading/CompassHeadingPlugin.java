package com.almufti.compassheading;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "CompassHeading")
public class CompassHeadingPlugin extends Plugin implements SensorEventListener {

    private SensorManager sensorManager;
    private float[] accelerometerData = new float[3];
    private float[] magnetometerData = new float[3];

    @Override
    public void load() {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @PluginMethod
    public void start(PluginCall call) {
        Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, magnet, SensorManager.SENSOR_DELAY_UI);

        call.resolve();
    }

    @PluginMethod
    public void stop(PluginCall call) {
        sensorManager.unregisterListener(this);
        call.resolve();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerData = event.values.clone();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerData = event.values.clone();
        }

        float[] rotationMatrix = new float[9];
        float[] orientation = new float[3];
        int accuracy = event.accuracy;

        if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerData, magnetometerData)) {
            SensorManager.getOrientation(rotationMatrix, orientation);
            float azimuth = (float) Math.toDegrees(orientation[0]);
            azimuth = (azimuth + 360) % 360;

            JSObject result = new JSObject();
            result.put("heading", azimuth);
            result.put("accuracy", accuracy);
            notifyListeners("headingChange", result);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @PluginMethod
    public void removeAllListeners(PluginCall call) {
        sensorManager.unregisterListener(this);
        call.resolve();
    }
}
