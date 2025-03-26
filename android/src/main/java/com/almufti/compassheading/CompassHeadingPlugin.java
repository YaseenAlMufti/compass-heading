package com.almufti.compassheading;

import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;

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
    private boolean useTrueNorth = false;
    private Location lastLocation = null;

    @Override
    public void load() {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
    }

    @PluginMethod
    public void start(PluginCall call) {
        useTrueNorth = call.getBoolean("useTrueNorth", false);

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

    @PluginMethod
    public void setLocation(PluginCall call) {
        double lat = call.getDouble("latitude", 0.0);
        double lon = call.getDouble("longitude", 0.0);

        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lon);
        lastLocation = location;

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

        if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerData, magnetometerData)) {
            SensorManager.getOrientation(rotationMatrix, orientation);
            float azimuth = (float) Math.toDegrees(orientation[0]);
            azimuth = (azimuth + 360) % 360;

            float finalHeading = azimuth;

            if (useTrueNorth && lastLocation != null) {
                GeomagneticField geoField = new GeomagneticField(
                    (float) lastLocation.getLatitude(),
                    (float) lastLocation.getLongitude(),
                    0f,
                    System.currentTimeMillis()
                );
                finalHeading = (azimuth + geoField.getDeclination() + 360) % 360;
            }

            JSObject result = new JSObject();
            result.put("heading", finalHeading);
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
