package be.cegeka.android.dwaaldetectie.model;

import java.io.IOException;
import java.util.List;
import android.location.Address;
import android.location.Geocoder;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import be.cegeka.android.dwaaldetectie.view.MapView;


public class SuggestionsThread extends Thread
{
	private MapView mapView;
	public boolean running;
	private List<Address> addresses;
	private AutoCompleteTextView autoCompleteTextView;

	public SuggestionsThread(MapView mapView)
	{
		this.mapView = mapView;
		this.running = true;
		this.autoCompleteTextView = mapView.textView;
	}


	public void run()
	{
		try
		{
			Geocoder geocoder = new Geocoder(mapView);

			while (running)
			{
				addresses = geocoder.getFromLocationName(autoCompleteTextView.getText().toString(), 5);
				
				if (true)
				{
					mapView.runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							mapView.arrayList.clear();
							
							for (int i = 0; i < addresses.size(); i++)
							{
								System.out.println(addresses.size());
								
								mapView.arrayList.add("lala");
								
								mapView.adapter.notifyDataSetChanged();
							}
						}
					});
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		
//		Geocoder geocoder = new Geocoder(mapView);
//		try
//		{
//			System.out.println(geocoder.getFromLocationName("b", 1).get(0).toString());
//			System.out.println(geocoder.getFromLocationName("bo", 1).get(0).toString());
//			System.out.println(geocoder.getFromLocationName("bon", 1).get(0).toString());
//			System.out.println(geocoder.getFromLocationName("bond", 1).get(0).toString());
//			System.out.println(geocoder.getFromLocationName("bondg", 1).get(0).toString());
//			System.out.println(geocoder.getFromLocationName("bondge", 1).get(0).toString());
//			System.out.println(geocoder.getFromLocationName("bondgen", 1).get(0).toString());
//			System.out.println(geocoder.getFromLocationName("bondgeno", 1).get(0).toString());
//			System.out.println(geocoder.getFromLocationName("bondgenot", 1).get(0).toString());
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
	}
}
