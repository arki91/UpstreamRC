package com.example.upstream;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class SensorActivity implements SensorEventListener {


	private MainActivity mainActivity= null;
	private SensorManager sensorManager=null;
	private Sensor lage = null;
	private float neigung =0;
	
	
	public SensorActivity(SensorManager sensorManager, Sensor lage, MainActivity mainActivity) {
		this.sensorManager=sensorManager;
		this.lage=lage;
		this.mainActivity=mainActivity;
		
	}
	
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		neigung= arg0.values[1];
		
		mainActivity.gibSensorwerte(neigung);

		
	}



}
