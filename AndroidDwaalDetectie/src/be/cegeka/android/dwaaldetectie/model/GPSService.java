package be.cegeka.android.dwaaldetectie.model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;


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
		super.onDestroy();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!running)
		{
			running = true;
			gpsService = this;
			lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, GPSConfig.changeListener);
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 1, GPSConfig.changeListener);
		}
		return START_STICKY;
	}
	
	
	public static GPSService getInstance()
	{
		return gpsService;
	}

}
