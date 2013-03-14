package be.cegeka.android.dwaaldetectie.model;

import be.cegeka.android.dwaaldetectie.view.MainActivity;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;

public class GPSService extends Service{

	private LocationManager lm;
	private static GPSService gpsService;
	public static boolean running;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	public void onDestroy() {
		lm.removeUpdates(GPSConfig.changeListener);
		running=false;
		Toast.makeText(this, "GPS Stopped", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStart(Intent intent, int startid)
	{
		if(!running){
			running=true;
			gpsService=this;
			lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, GPSConfig.changeListener);
			Toast.makeText(this, "GPS Started", Toast.LENGTH_LONG).show();
		}
	}

	//	@Override
	//	public int onStartCommand(Intent intent, int flags, int startId) {
	//	    return START_STICKY;
	//	}

	public static GPSService getInstance(){
		return gpsService;
	}

}
