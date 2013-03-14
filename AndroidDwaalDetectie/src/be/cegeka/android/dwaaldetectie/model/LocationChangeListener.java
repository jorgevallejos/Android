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
		LocationChangeListener.context = context;
	}


	@Override
	public void onLocationChanged(Location location)
	{
		if (ApplicationLogic.location != null)
		{
			((MainActivity) context).updateDistance(ApplicationLogic.location.distanceTo(location));
		}

		if (ApplicationLogic.location != null && ApplicationLogic.location.distanceTo(location) > 2000)
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
