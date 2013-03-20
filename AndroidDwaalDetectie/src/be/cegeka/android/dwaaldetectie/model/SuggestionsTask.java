package be.cegeka.android.dwaaldetectie.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import be.cegeka.android.dwaaldetectie.view.MapView;


public class SuggestionsTask extends AsyncTask<MapView, Void, MapView>
{
	Geocoder geocoder;
	List<String> addresses;


	@Override
	protected void onPreExecute()
	{

		super.onPreExecute();
	}


	@Override
	protected MapView doInBackground(MapView... params)
	{
		MapView mapView = params[0];

		geocoder = new Geocoder(mapView);
		addresses = new ArrayList<String>();
		List<Address> addressesResult = null;

		try
		{
			addressesResult = geocoder.getFromLocationName(mapView.textView.getText().toString() + ", belgium", 20);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (addressesResult != null && !addressesResult.isEmpty())
		{
			for (Address address : addressesResult)
			{
				addresses.add(AddressConverter.convertAddress(address));
			}
		}

		return mapView;
	}


	@Override
	protected void onPostExecute(MapView result)
	{
		super.onPostExecute(result);

		result.adapter.clear();

		if (addresses != null)
		{
			for (String adres : addresses)
			{
				result.adapter.add(adres);
			}
		}

		result.adapter.notifyDataSetChanged();
	}

}
