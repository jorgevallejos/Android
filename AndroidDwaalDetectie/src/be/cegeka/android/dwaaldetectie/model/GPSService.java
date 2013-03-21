package be.cegeka.android.dwaaldetectie.model;

import static be.cegeka.android.dwaaldetectie.model.GPSConfig.getGPSConfig;
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
	private Timer timer;
	private static boolean running;
	private static boolean lmRunning;


	public GPSService()
	{
		timer = new Timer();
		running = false;
		lmRunning = true;
	}


	public static boolean isRunning()
	{
		return running;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		if (!running)
		{
			running = true;
			lmRunning = true;

			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, getGPSConfig().getChangeListener());
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, getGPSConfig().getChangeListener());

			timer.scheduleAtFixedRate(new TimerTask()
			{
				Handler handler;


				@Override
				public void run()
				{
					if (lmRunning)
					{
						lm.removeUpdates(getGPSConfig().getChangeListener());
						
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
								lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, getGPSConfig().getChangeListener());
								lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, getGPSConfig().getChangeListener());
							}
						});
						
						lmRunning = true;
					}
				}
			}, 10000, 10000);
		}
		
		return START_STICKY;
	}


	public void onDestroy()
	{
		GPSConfig.getGPSConfig().setDistanceInfo("");
		lm.removeUpdates(getGPSConfig().getChangeListener());
		timer.cancel();
		
		running = false;
		lmRunning = false;
		
		super.onDestroy();
	}


	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
}
