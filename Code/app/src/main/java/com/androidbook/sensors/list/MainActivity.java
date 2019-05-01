package com.androidbook.sensors.list;

//This file is MainActivity.java
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button playBtn;
	SeekBar positionBar;
	SeekBar volumeBar;
	TextView torchText;
	TextView lightsensorValue;

	private SensorManager sensorManager;
	private Sensor lightSensor;
	private SensorEventListener lightSensorListener;
	public static Camera cam;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		torchText = (TextView) findViewById(R.id.torchText);
		lightsensorValue = (TextView) findViewById(R.id.lightsensor);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


		if (lightSensor == null) {
			Toast.makeText(this, "light sensor is not available !", Toast.LENGTH_LONG).show();
			finish();
		}

		lightSensorListener = new SensorEventListener() {
			public void onSensorChanged(SensorEvent sensorEvent) {
				try {
					if (sensorEvent.values[0]<=10) {
						cam=Camera.open();
						Camera.Parameters p = cam.getParameters();
						p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
						torchText.setText("TORCH ON");
						lightsensorValue.setText("Light Sensor Value : "+sensorEvent.values[0]);
						cam.setParameters(p);
						cam.startPreview();
					} else {
						torchText.setText("TORCH OFF");
						lightsensorValue.setText("Light Sensor Value : "+sensorEvent.values[0]);
						Camera.Parameters p = cam.getParameters();
						cam.setParameters(p);
						cam.stopPreview();
						cam.release();
					}
				}catch(Exception e){
					Log.d("Exception", "instance initializer: "+e);
				}

			}

			public void onAccuracyChanged(Sensor sensor, int i) {
			}
		};
	}


	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(lightSensorListener, lightSensor,
				2 * 1000 * 1000);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(lightSensorListener);
	}





}