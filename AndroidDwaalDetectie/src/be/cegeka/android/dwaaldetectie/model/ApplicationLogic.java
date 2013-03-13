package be.cegeka.android.dwaaldetectie.model;

import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;


public class ApplicationLogic
{
	private Context context;
	public static Location location;
	
	
	public ApplicationLogic(Context context)
	{
		this.context = context;
		ApplicationLogic.location = new Location("hier");
	}


	public String addressFromLocation(Location location)
	{
		String address = null;
		
		try
		{
			Geocoder geo = new Geocoder(context.getApplicationContext(), Locale.getDefault());
			List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			
			if (addresses.isEmpty())
			{
				address = "Waiting for location";
			}
			else
			{
				if (addresses.size() > 0)
				{
					address = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(); // getFromLocation() may sometimes fail
		}
		
		return address;
	}


	public Location locationFromAddress(String address)
	{
		Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		Location baseLocation = null;
		
		try
		{
			List<Address> addresses = geoCoder.getFromLocationName(address, 1);
			if (addresses.size() > 0)
			{
				baseLocation = new Location("GPS");
				Address a = addresses.get(0);
				System.out.println("Longitude Base Address: " + a.getLongitude());
				System.out.println("Latitude Base Address: " + a.getLatitude());
				baseLocation.setLatitude(addresses.get(0).getLatitude());
				baseLocation.setLongitude(addresses.get(0).getLongitude());
			}
		}
		catch (Exception ee)
		{
			ee.printStackTrace();
		}
		
		return baseLocation;
	}
}
