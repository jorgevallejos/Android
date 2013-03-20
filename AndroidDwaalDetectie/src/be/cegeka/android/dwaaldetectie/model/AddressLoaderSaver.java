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
	public static void saveAddress(Context ctx, LatLng latLng, String address, long distance) throws IOException
	{
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat("latitude", (float) latLng.latitude);
		editor.putFloat("longitude", (float) latLng.longitude);
		editor.putString("address", address);
		editor.putLong("distance", distance);
		editor.commit();
	}
	
	
	public static void saveDistance(Context ctx, long distance)
	{
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("distance", distance);
		editor.commit();
	}


	/**
	 * Load all the numbers saved by this app.
	 * 
	 * @return An {@link ArrayList} of the cell-phone numbers.
	 */
	public static LatLng loadAddress(Context ctx) throws Exception
	{
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);

		if (!(settings.contains("latitude") || settings.contains("longitude")))
		{
			throw new Exception();
		}

		double latitude = settings.getFloat("latitude", 0);
		double longitude = settings.getFloat("longitude", 0);

		return new LatLng(latitude, longitude);
	}


	public static String loadAddressDescription(Context ctx) throws Exception
	{
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);

		if (!settings.contains("address"))
		{
			throw new Exception();
		}

		return settings.getString("address", null);
	}


	public static long loadAddressDistance(Context context) throws Exception
	{
		SharedPreferences settings = context.getSharedPreferences("file", 0);

		if (!settings.contains("distance"))
		{
			throw new Exception();
		}

		return settings.getLong("distance", 0);
	}
}
