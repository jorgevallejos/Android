package be.cegeka.android.dwaaldetectie.model;

import static be.cegeka.android.dwaaldetectie.model.TrackingConfiguration.trackingConfig;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;


public class TrackingService extends Service
{
	private LocationManager lm;
	private Timer timer;
	private static boolean running;
	private static boolean receivingLocationUpdates;


	public TrackingService()
	{
		timer = new Timer();
		running = false;
		receivingLocationUpdates = true;
	}


	/**
	 * Returns if the Tracking Service is currently running or not.
	 * 
	 * @return True if the Service is running, false if the Service is not
	 *         running.
	 */
	public static boolean isRunning()
	{
		return running;
	}


	/**
	 * This method is called when the TrackingService is started using an
	 * Intent. It will start the service and periodically check for the current
	 * location of the user.
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!running)
		{
			running = true;
			receivingLocationUpdates = true;

			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, trackingConfig().getChangeListener());
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, trackingConfig().getChangeListener());

			timer.scheduleAtFixedRate(new TimerTask()
			{
				Handler handler;


				@Override
				public void run()
				{
					if (receivingLocationUpdates)
					{
						lm.removeUpdates(trackingConfig().getChangeListener());

						receivingLocationUpdates = false;
					}
					else
					{
						handler = new Handler(Looper.getMainLooper());
						handler.post(new Runnable()
						{
							@Override
							public void run()
							{
								lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, trackingConfig().getChangeListener());
								lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, trackingConfig().getChangeListener());
							}
						});

						receivingLocationUpdates = true;
					}
				}
			}, 10000, 10000);
		}

		return START_STICKY;
	}


	/**
	 * This method is called when the TrackingService is stopped using an
	 * Intent.
	 */
	public void onDestroy()
	{
		TrackingConfiguration.trackingConfig().setDistanceInfo("");
		lm.removeUpdates(trackingConfig().getChangeListener());
		timer.cancel();

		running = false;
		receivingLocationUpdates = false;

		super.onDestroy();
	}


	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
}
