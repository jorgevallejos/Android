package be.cegeka.android.dwaaldetectie.model;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.maps.model.LatLng;


public class AddressLoaderSaver
{

	/**
	 * Save the numbers. The numbers file will be completely overwritten.
	 * 
	 * @param selectedNumbers
	 *            An {@link ArrayList} of Strings representing the numbers that
	 *            need to be saved.
	 * @throws IOException
	 */
	public static void saveAddress(Context ctx, LatLng latLng, String address, long maxDistance) throws IOException
	{
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat("latitude", (float) latLng.latitude);
		editor.putFloat("longitude", (float) latLng.longitude);
		editor.putString("address", address);
		editor.putLong("distance", maxDistance);
		editor.commit();
	}
	
	
	public static void saveMaxDistance(Context ctx, long maxDistance)
	{
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("distance", maxDistance);
		editor.commit();
	}


	/**
	 * Load all the numbers saved by this app.
	 * 
	 * @return An {@link ArrayList} of the cell-phone numbers.
	 */
	public static LatLng loadAddress(Context ctx)
	{
		LatLng latLng = null;
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);

		if (settings.contains("latitude") && settings.contains("longitude"))
		{
			double latitude = settings.getFloat("latitude", 0);
			double longitude = settings.getFloat("longitude", 0);

			latLng = new LatLng(latitude, longitude);
		}

		return latLng;
	}


	public static String loadAddressDescription(Context ctx)
	{
		String description = null;
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);

		if (settings.contains("address"))
		{
			description = settings.getString("address", null);
		}

		return description;
	}


	public static long loadMaxDistance(Context context)
	{
		long maxDistance = -1;
		SharedPreferences settings = context.getSharedPreferences("file", 0);

		if (settings.contains("distance"))
		{
			maxDistance = settings.getLong("distance", 0);
		}

		return maxDistance;
	}
}
