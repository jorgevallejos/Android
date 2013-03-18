package be.cegeka.android.dwaaldetectie.model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;


public class GPSService extends Service
{

	private LocationManager lm;
	private static GPSService gpsService;
	public static boolean running;


	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}


	public void onDestroy()
	{
		GPSConfig.setDistanceInfo("");
		lm.removeUpdates(GPSConfig.changeListener);
		running = false;
		Toast.makeText(this, "GPS Stopped", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!running)
		{
			running = true;
			gpsService = this;
			lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, GPSConfig.changeListener);
			Toast.makeText(this, "GPS Started", Toast.LENGTH_LONG).show();
		}
		return START_STICKY;
	}
	
	public static GPSService getInstance()
	{
		return gpsService;
	}

}
