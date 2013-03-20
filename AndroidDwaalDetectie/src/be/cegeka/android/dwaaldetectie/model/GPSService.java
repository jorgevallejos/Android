package be.cegeka.android.dwaaldetectie.model;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;


public class GPSService extends Service
{

	private LocationManager lm;
	private static GPSService gpsService;
	public static boolean running;
	private boolean lmRunning;
	private Timer timer = new Timer();


	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}


	public void onDestroy()
	{
		GPSConfig.setDistanceInfo("");
		lm.removeUpdates(GPSConfig.changeListener);
		timer.cancel();
		running = false;
		lmRunning = false;
		super.onDestroy();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		if (!running)
		{
			running = true;
			gpsService = this;
			lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, GPSConfig.changeListener);
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, GPSConfig.changeListener);
			lmRunning = true;

			timer.scheduleAtFixedRate(new TimerTask()
			{
				Handler handler;
				
				@Override
				public void run()
				{
					if (lmRunning)
					{
						lm.removeUpdates(GPSConfig.changeListener);
						lmRunning = false;
					}
					else
					{
						handler = new Handler(Looper.getMainLooper());
						handler.post(new Runnable()
						{
							@Override
							public void run()
							{
								lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, GPSConfig.changeListener);
								lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, GPSConfig.changeListener);
							}
						});
						lmRunning = true;
					}
				}
			}, 10000, 10000);
		}
		return START_STICKY;
	}


	public static GPSService getInstance()
	{
		return gpsService;
	}

}
