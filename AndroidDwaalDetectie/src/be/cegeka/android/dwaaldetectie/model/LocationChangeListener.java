package be.cegeka.android.dwaaldetectie.model;

import android.content.Context;
import android.database.Observable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;


public class LocationChangeListener extends Observable implements LocationListener
{
	private Context context;
	private Location baseLocation;
	
	
	public LocationChangeListener(Context context, Location baseLocation)
	{
		this.context = context;
		this.baseLocation = baseLocation;
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

		System.out.println(baseLocation.distanceTo(location));

		if (baseLocation != null && baseLocation.distanceTo(location) > 2000)
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
