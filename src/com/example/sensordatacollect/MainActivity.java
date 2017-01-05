package com.example.sensordatacollect;

import java.io.FileNotFoundException;
import java.util.Vector;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private Button startRecordBtn = null;
	private Button stopRecordBtn = null;
	private TextView dataView = null;
	private SensorManager mSensorManager;
	private Sensor mAccelerometerSensor;
	private Sensor mGravitySensor;
	private Sensor mGryoscopeSensor;
	private Sensor mLinear_AccelerationSensor;
	private Sensor mRotation_vectorSensor;
	
	private Vector<Scalar4> mAccelerometer = new Vector<Scalar4>();
	private Vector<Scalar4> mGravity = new Vector<Scalar4>();
	private Vector<Scalar4> mGryoscope = new Vector<Scalar4>();
	private Vector<Scalar4> mLinear_Acceleration = new Vector<Scalar4>();
	private Vector<Scalar4> mRotation_vector = new Vector<Scalar4>();

	private SensorEventListener mSensorListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			SensorType sensorType = SensorType.Undifined;
			Scalar4 data = null;
			if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
			    data = new Scalar4(event.values[0], event.values[1],event.values[2], event.values[3]);
			else 
				data = new Scalar4(event.values[0], event.values[1],event.values[2]);
			
			switch(event.sensor.getType())
			{
				case Sensor.TYPE_ACCELEROMETER:
					mAccelerometer.add(data);
					sensorType = SensorType.ACCELEROMETER;
					break;
				case Sensor.TYPE_GRAVITY:
					mGravity.add(data);
					sensorType = SensorType.GRAVITY;
					break;
				case Sensor.TYPE_GYROSCOPE:
					mGryoscope.add(data);
					sensorType = SensorType.GYROSCOPE;
					break;
				case Sensor.TYPE_LINEAR_ACCELERATION:
					mLinear_Acceleration.add(data);
					sensorType = SensorType.LINEAR_ACCELERATION;
					break;
				case Sensor.TYPE_ROTATION_VECTOR:
					mRotation_vector.add(data);
					sensorType = SensorType.ROTATION_VECTOR;
					break;
				default:
					break;
			}
			StringBuilder sb = new StringBuilder(sensorType.getName());
			SerializeTask.ToStringUtils(sb, data, sensorType);
			dataView.append(sb.toString());
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startRecordBtn = (Button) this.findViewById(R.id.start_record_btn);
		stopRecordBtn = (Button) this.findViewById(R.id.stop_record_btn);
		dataView = (TextView) this.findViewById(R.id.data_view);
		dataView.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		stopRecordBtn.setEnabled(false);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
		mGryoscopeSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		mLinear_AccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mRotation_vectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

		startRecordBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				RegisterSensorListeners();
				
				startRecordBtn.setEnabled(false);
				stopRecordBtn.setEnabled(true);
				dataView.setText("");
			}
		});
		
		stopRecordBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UnRegisterSensorListeners();
				
				//todo :: save the text to file
				SerializeTask writeAcc;
				try {
					writeAcc = new SerializeTask("Accelerometer.txt", mAccelerometer, SensorType.ACCELEROMETER);
					writeAcc.start();
					
					SerializeTask writeGra = new SerializeTask("Gravity.txt", mGravity, SensorType.GRAVITY);
					writeGra.start();
					
					SerializeTask writeGry = new SerializeTask("Gryoscope.txt", mGryoscope, SensorType.GYROSCOPE);
					writeGry.start();
					
					SerializeTask writeLineAcc = new SerializeTask("Linear_Acceleration.txt", mLinear_Acceleration, SensorType.LINEAR_ACCELERATION);
					writeLineAcc.start();
					
					SerializeTask writeRvec = new SerializeTask("Rotation_vector.txt", mRotation_vector, SensorType.ROTATION_VECTOR);
					writeRvec.start();	
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				stopRecordBtn.setEnabled(false);
				startRecordBtn.setEnabled(true);
			}
		});
	}
	
	private void UnRegisterSensorListeners()
	{
		mSensorManager.unregisterListener(mSensorListener, mAccelerometerSensor);
		mSensorManager.unregisterListener(mSensorListener, mGravitySensor);
		mSensorManager.unregisterListener(mSensorListener, mGryoscopeSensor);
		mSensorManager.unregisterListener(mSensorListener, mLinear_AccelerationSensor);
		mSensorManager.unregisterListener(mSensorListener, mRotation_vectorSensor);
	}
	
	private void RegisterSensorListeners()
	{
		mSensorManager.registerListener(mSensorListener, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL); //以普通采样率注册监听器
		mSensorManager.registerListener(mSensorListener, mGravitySensor,
		                SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(mSensorListener, mGryoscopeSensor,
						 SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(mSensorListener, mLinear_AccelerationSensor,
			 SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(mSensorListener, mRotation_vectorSensor,
			 SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	
	
    @Override 
    protected void onDestroy() {
        super.onDestroy();
        UnRegisterSensorListeners();
    }
}
