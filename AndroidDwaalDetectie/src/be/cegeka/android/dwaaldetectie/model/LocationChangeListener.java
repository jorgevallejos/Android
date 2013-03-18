package be.cegeka.android.dwaaldetectie.model;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.view.MainActivity;


public class LocationChangeListener implements LocationListener
{
	private Context context;
	private Toast toast;

	@SuppressLint("ShowToast")
	public LocationChangeListener(Context context)
	{
		toast = Toast.makeText(context, "Too far", Toast.LENGTH_LONG);
		this.context = context;
	}


	@Override
	public void onLocationChanged(Location location)
	{

		if (GPSConfig.getLocation() != null)
		{
			GPSConfig.setDistance(location);
			MainActivity.updateDistance();
		
			Location location2 = new Location("");
			location2.setLatitude(GPSConfig.getLocation().latitude);
			location2.setLongitude(GPSConfig.getLocation().longitude);
			
			if (location.distanceTo(location2) > 2000)
			{
				toast.show();
			}
			else
			{
				toast.cancel();
			}
		}
	}


	@Override
	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub

	}


	@Override
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub

	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub

	}
}
