package com.example.upstream;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity   {

	private final int DEFAULT =50;
	
	private byte lenkung=127;
	private byte geschwindigkeit=127;
	private byte aufloesung;
	private byte farbe=1;
	private byte qualitaet=50;
	private byte fps=16;
	
	private SensorManager snesorManager=null;
	private Sensor lagesensor=null;
	private SensorActivity sensorActivity=null;
	private SeekBar lenkungSeek;
	private SeekBar geschwindigkeitSeek;
	private TextView uebertragungsrate;
	private TextView bildrate;
	private TextView bildgroesse;
	private ImageView imageView;

	private Spinner qualitaetspin;
	private Spinner aufloesungSpin;
	private Spinner fpsspinn;

	private UDPTester test;
	private CheckBox farbcheck;
	
	private Rechnungen rechnen;
	
	private PowerManager pm;
	private WakeLock wk;

	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  
        
        pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
        wk = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyTag");
        wk.acquire();

        
        rechnen = new Rechnungen();
        snesorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
      
        
        test = new UDPTester(this);
        
        
        lenkungSeek= (SeekBar)findViewById(R.id.seekBar1);
        lenkungSeek.setEnabled(false);
        lenkungSeek.setProgress(DEFAULT);
        
        uebertragungsrate=(TextView)findViewById(R.id.textView1);
        bildgroesse=(TextView)findViewById(R.id.textView2);
        bildrate=(TextView)findViewById(R.id.textView3);
        imageView=(ImageView)findViewById(R.id.imageView1);
        
        
        
        
        qualitaetspin =(Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence>adapterquali = ArrayAdapter.createFromResource(this, R.array.spinner_quali, android.R.layout.simple_spinner_item);
        adapterquali.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualitaetspin.setAdapter(adapterquali);
        qualitaetspin.setSelection(0);
        qualitaetspin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				qualitaet=(byte)(5*arg2);
				//test.sendImgParam(aufloesung, fps, qualitaet, farbe);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        aufloesungSpin =(Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence>adapterauf = ArrayAdapter.createFromResource(this, R.array.aufloesung, android.R.layout.simple_spinner_item);
        adapterauf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aufloesungSpin.setAdapter(adapterauf);
        aufloesungSpin.setSelection(0);
        aufloesungSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				aufloesung=(byte)arg2;
				test.sendImgParam(aufloesung, fps, qualitaet, farbe);				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        fpsspinn =(Spinner)findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence>adapterfps = ArrayAdapter.createFromResource(this, R.array.spinner_fps, android.R.layout.simple_spinner_item);
        adapterfps.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fpsspinn.setAdapter(adapterfps);
        fpsspinn.setSelection(9);
        fpsspinn.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				fps=(byte)(2*arg2);
				test.sendImgParam(aufloesung, fps, qualitaet, farbe);				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

        

       
        
        geschwindigkeitSeek=(SeekBar)findViewById(R.id.seekBar2);
        geschwindigkeitSeek.setProgress(DEFAULT);
        geschwindigkeitSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				geschwindigkeitSeek.setProgress(progress);
				rechnen.analysiere(progress);
				
				
			}
		});
        
        farbcheck =(CheckBox)findViewById(R.id.checkBox1);
        farbcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					farbe =1;
					test.sendImgParam(aufloesung, fps, qualitaet, farbe);					
				}
				else {
					farbe =0;
					test.sendImgParam(aufloesung, fps, qualitaet, farbe);				}
				
			}
		});
        
        List<Sensor> sensoren = snesorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        lagesensor = null;
        for(Sensor s:sensoren) {
        	if(s.getType()==Sensor.TYPE_ORIENTATION) {
        		lagesensor=s;
        		sensorActivity= new SensorActivity(snesorManager, lagesensor, this);
        	}
        }
       
    
    	imageView.setImageResource(R.drawable.android);
    }
    
    public void setTextViews(String fps) {
    	//Uebertragungsrate
    	//uebertragugnsrate.setText(state.getImgRate()+" kb/s");
    	//uebertragungsrate.setText(kbs);
    	//Bildgroeße
    	//bildgroesse.setText(groesse);
    	//bildgroesse.setText(state.getImgSize()+" Byte");
    	//Bildrate
    	bildrate.setText(fps);
    	//bildrate.setText(state.getImgRate()+" FPS");
    }

	
	public void updateImageView(Bitmap image) {
		imageView.setImageBitmap(image);
	}

    
    public byte getGeschwindigkeit(){
    	return geschwindigkeit;
    }
    
    public byte getLenkung() {
    	return lenkung;
    }
    

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		wk.release();
		snesorManager.unregisterListener(sensorActivity);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		wk.acquire();
		super.onResume();
		snesorManager.registerListener(sensorActivity, lagesensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
  
	public void gibSensorwerte(float neigung) {
    	int temp;
		temp = rechnen.berechneXrAusX(-neigung);
   	 	int progressWert = rechnen.progressBarAusX(temp);
   	 	lenkungSeek.setProgress(progressWert);
   	 	lenkung=(byte)temp;
   	 	bildgroesse.setText(""+lenkung);
    }
 
}
