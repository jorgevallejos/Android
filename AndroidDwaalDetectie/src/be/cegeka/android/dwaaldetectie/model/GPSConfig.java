package be.cegeka.android.dwaaldetectie.model;

import java.io.IOException;
import java.text.DecimalFormat;
import android.content.Context;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;


public class GPSConfig
{
	public static String address;
	public static LocationChangeListener changeListener;
	private static String distance = "";
	private static LatLng location;
	
	
	public static String getDistance()
	{
		return distance;
	}
	
	
	public static LatLng getLocation()
	{
		return location;
	}
	
	
	public static void setLocation(Context ctx, LatLng latLng) throws IOException
	{
		location = latLng;
	    AddressLoaderSaver.saveAddress(ctx, latLng, address);
	}
	
	
	public static void setDistance(Location currentLocation)
	{
		Location homeLocation = new Location("homeLocation");
		
		homeLocation.setLatitude(location.latitude);
		homeLocation.setLongitude(location.longitude);
		
		float distanceTo = homeLocation.distanceTo(currentLocation);
		
		String result = null;

		if (distanceTo < 1000)
		{
			DecimalFormat decimalFormat = new DecimalFormat("#");
			result = decimalFormat.format(distanceTo) + " m";
		}
		else
		{
			DecimalFormat decimalFormat = new DecimalFormat("#.#");
			distanceTo = distanceTo / 1000;
			result = decimalFormat.format(distanceTo) + " km";
		}
		
		distance = result;
	}
	
	
	public static void setDistanceInfo(String distanceInfo)
	{
		distance = distanceInfo;
	}
}
