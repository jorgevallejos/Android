package be.cegeka.android.dwaaldetectie.model;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Observable;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import be.cegeka.android.dwaaldetectie.utilities.LatLngLocationConverter;
import be.cegeka.android.dwaaldetectie.view.listeners.LocationChangeListener;
import com.google.android.gms.maps.model.LatLng;


public class TrackingConfiguration extends Observable
{
	/**
	 * Result variables for starting the service
	 */
	public static int RESULT_OK = 0;
	public static int RESULT_ERROR = 1;
	public static int RESULT_NO_ADDRESS_SET = 2;
	public static int RESULT_NO_MAX_DISTANCE = 3;

	/**
	 * Instance of GPSConfig (SINGLETON)
	 */
	private static final TrackingConfiguration INSTANCE = new TrackingConfiguration();

	private LocationChangeListener changeListener;
	private LatLng location;
	private String address;
	private String distance = "";
	private long maxDistance;


	private TrackingConfiguration()
	{
		/**
		 * Construcotor needs to be private (SINGLETON).
		 */
	}


	/**
	 * 
	 * @return The one instance of TrackingConfiguration.
	 */
	public static TrackingConfiguration trackingConfig()
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
		this.distance = distanceInfo;
	}


	/**
	 * Sets the LatLng location instance variable and also saves this value to
	 * the device.
	 * 
	 * @param ctx
	 * @param latLng
	 * @throws IOException
	 */
	public void setLocation(Context ctx, LatLng latLng) throws IOException
	{
		this.location = latLng;
		AddressLoaderSaver.saveAddress(ctx, latLng, address, maxDistance);
	}


	/**
	 * Updates the distance between the current location and the home location.
	 * Notifies its observers of a change in distance.
	 * 
	 * @param currentLocation
	 *            Current location of the user.
	 */
	public void updateDistance(Location currentLocation)
	{
		Location homeLocation = LatLngLocationConverter.locationFromLatLng(location);

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


	/**
	 * Tries to start the TrackingService. Tries to load the home location from
	 * the device. Return the result as an int.
	 * 
	 * @param context
	 *            Context of the Application.
	 * @return 0 if the TrackingService was started, 1 if an unexpected error
	 *         occurred, 2 if no address was stored on the device, 3 if no
	 *         maximum distance was stored on the device.
	 */
	public int startTrackingService(Context context)
	{
		int result;

		LatLng latLng = AddressLoaderSaver.loadAddress(context);
		address = AddressLoaderSaver.loadAddressDescription(context);
		maxDistance = AddressLoaderSaver.loadMaxDistance(context);
		if (latLng == null || address == null)
		{
			result = RESULT_NO_ADDRESS_SET;
		}
		else if(maxDistance == -1)
		{
			result = RESULT_NO_MAX_DISTANCE;
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

			context.startService(new Intent(context, TrackingService.class));
			result = RESULT_OK;
		}

		return result;
	}
}
