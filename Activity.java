import android.os.Bundle;
import android.app.Activity;
import android.hardware.Sensor;
import android.view.View;
import android.hardware.SensorEventListener;
import android.widget.RelativeLayout;
import android.hardware.SensorEvent;
import android.widget.Toast;
import android.hardware.SensorManager;

/*
 * This activity registers for proximity sensor and makes screen On/Off depending on sensor value
 */
 
public class ProximityActivity extends Activity implements SensorEventListener{

	private SensorManager sensorMngr ;
	private Sensor proximitySensor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_proximity);
		
		//get sensor service instance
		sensorMngr = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		//get the proximity sensor instance
		proximitySensor = sensorMngr.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		
		if(proximitySensor != null){
			// register a listener for proximity detection
			sensorMngr.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
		else{
			//if device do not have proximity sensor then toast it and finish this activity
			Toast.makeText(this, "Proximity Sensor Not Found , closing the application", Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		//Do your application changes here if sensor accuracy changes
	}

	/*
	 * This method gets called when sensor values are changed
	 * @param android.hardware.SensorEvent
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {		
		if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){
			// calls turnScreenOnOff method if sensor type is proximity
			turnScreenOnOff(event.values[0],proximitySensor.getMaximumRange());
		}
	}

	/*
	 * This method makes the current screen On/Off depending on sensor value
	 * @param sensorValue,maximumRange
	 */
	private void turnScreenOnOff(float sensorValue, float maximumRange) {
		RelativeLayout mainScreen = (RelativeLayout) findViewById(R.id.mainScreen);
		
		//get the current layout reference 
		if(sensorValue == maximumRange){
			//Show the current screen and toast a respective message when distance is maximum
			mainScreen.setVisibility(View.VISIBLE);
			Toast.makeText(this, "Turn On the Screen", Toast.LENGTH_SHORT).show();
		}
		else{
			//Turn off the screen and toast a message for same when distance is less than maximum
			mainScreen.setVisibility(View.GONE);
			Toast.makeText(this, "Turn Off the Screen", Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//unregister sensor listener on activity destroy 
		if(proximitySensor != null)
			sensorMngr.unregisterListener(this);
	}


}
