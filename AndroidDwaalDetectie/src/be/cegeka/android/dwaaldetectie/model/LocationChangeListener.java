package be.cegeka.android.dwaaldetectie.model;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.view.MainActivity;


public class LocationChangeListener implements LocationListener
{
	private static Context context;


	public LocationChangeListener(Context context)
	{
		this.context = context;
	}


	@Override
	public void onLocationChanged(Location location)
	{
		// Get Longitude and latitude from current location.
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();

		// Print to console
		System.out.println("Longitude Current Address: " + longitude);
		System.out.println("Latitude Current Address: " + latitude);
		if (GPSConfig.location != null && MainActivity.interfaceup)
		{
			System.out.println(GPSConfig.location.distanceTo(location));
			MainActivity.updateDistance(GPSConfig.location.distanceTo(location));
		}

		if (GPSConfig.location != null && GPSConfig.location.distanceTo(location) > 2000)
		{
			Toast.makeText(context, "Too far", Toast.LENGTH_LONG).show();
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
