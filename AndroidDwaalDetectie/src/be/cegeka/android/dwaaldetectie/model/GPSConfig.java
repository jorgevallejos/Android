package be.cegeka.android.dwaaldetectie.model;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Observable;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import be.cegeka.android.dwaaldetectie.view.listeners.LocationChangeListener;
import com.google.android.gms.maps.model.LatLng;


public class GPSConfig extends Observable
{
	/**
	 * Result variables for starting the service
	 */
	public int RESULT_OK = 0;
	public int RESULT_ERROR = 1;
	public int RESULT_NO_ADDRESS_SET = 2;

	/**
	 * Instance of GPSConfig (SINGLETON)
	 */
	private static final GPSConfig INSTANCE = new GPSConfig();

	private LocationChangeListener changeListener;
	private LatLng location;
	private String address;
	private String distance = "";
	private long maxDistance;


	private GPSConfig()
	{
		/**
		 * Construcotor needs to be private (SINGLETON).
		 */
	}


	public static GPSConfig getGPSConfig()
	{
		return INSTANCE;
	}


	public String getAddress()
	{
		return address;
	}


	public long getMaxDistance()
	{
		return maxDistance;
	}


	public String getDistance()
	{
		return distance;
	}


	public LatLng getLocation()
	{
		return location;
	}


	public LocationChangeListener getChangeListener()
	{
		return changeListener;
	}


	public void setMaxDistance(long maxDistance)
	{
		this.maxDistance = maxDistance;
	}


	public void setAddress(String address)
	{
		this.address = address;
	}


	public void setDistanceInfo(String distanceInfo)
	{
		distance = distanceInfo;
	}


	public void setLocation(Context ctx, LatLng latLng) throws IOException
	{
		location = latLng;
		AddressLoaderSaver.saveAddress(ctx, latLng, address, maxDistance);
	}


	public void setDistance(Location currentLocation)
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
		setChanged();
		notifyObservers();
	}


	public int startTrackingService(Context context)
	{
		int result;

		LatLng latLng = AddressLoaderSaver.loadAddress(context);
		address = AddressLoaderSaver.loadAddressDescription(context);
		maxDistance = AddressLoaderSaver.loadMaxDistance(context);
		if (latLng == null || address == null || maxDistance == -1)
		{
			result = RESULT_NO_ADDRESS_SET;
		}
		else
		{
			try
			{
				setLocation(context, latLng);
			}
			catch (Exception e)
			{
				result = RESULT_ERROR;
			}
			if (changeListener == null)
			{
				changeListener = new LocationChangeListener(context);
			}

			context.startService(new Intent(context, GPSService.class));
			result = RESULT_OK;
		}

		return result;
	}
}
