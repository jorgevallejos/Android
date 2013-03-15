package be.cegeka.android.dwaaldetectie.model;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
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
		createNotification();
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
	
	@SuppressLint("NewApi")
	public void createNotification() {
	    // Prepare intent which is triggered if the
	    // notification is selected
	    //Intent intent = new Intent(context, NotificationReceiverActivity.class);
	    //PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    Notification noti = new Notification.Builder(context).build();
	    // Hide the notification after its selected
	    noti.flags |= Notification.FLAG_AUTO_CANCEL;

	    notificationManager.notify(0, noti);

	  }
}
