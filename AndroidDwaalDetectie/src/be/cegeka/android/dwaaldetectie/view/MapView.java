package be.cegeka.android.dwaaldetectie.view;

import java.io.IOException;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.R;
import be.cegeka.android.dwaaldetectie.model.GPSConfig;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapView extends Activity
{
	private LatLng latLng;
	private GoogleMap map;
	private String message;
	private String message2;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);

		setUpMap();
	}


	private void setUpMap()
	{
		if (map == null)
		{
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		if (map != null)
		{

			map.setMyLocationEnabled(true);
			map.setOnMapLongClickListener(new MyMapLongClickListener());
			map.setOnInfoWindowClickListener(new MyInfoWindowClickListener());
		}
	}

	public class MyMapLongClickListener implements OnMapLongClickListener
	{
		@Override
		public void onMapLongClick(LatLng point)
		{
			if (!isOnline())
			{
				Toast.makeText(MapView.this, R.string.map_info_no_internet, Toast.LENGTH_SHORT).show();
			}
			else
			{
				latLng = point;
				map.clear();

				Geocoder geocoder = new Geocoder(MapView.this);
				try
				{
					List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
					if (addresses.size() > 0)
					{
						Address address = geocoder.getFromLocation(point.latitude, point.longitude, 1).get(0);
						message = address.getAddressLine(0);
						if (address.getAddressLine(1) != null)
						{
							message += ", " + address.getAddressLine(1);
							if (address.getAddressLine(2) != null)
							{
								message += ", " + address.getAddressLine(2);
							}

						}
						message2 = message;
					}
					else
					{
						message = getString(R.string.map_marker_message_no_address_found);
						message2 = latLng.latitude + ", " + latLng.longitude;
					}

					MarkerOptions markerOptions = new MarkerOptions();
					markerOptions.position(point);
					markerOptions.title(message);
					markerOptions.draggable(false);
					Marker marker = map.addMarker(markerOptions);
					marker.showInfoWindow();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public class MyInfoWindowClickListener implements OnInfoWindowClickListener
	{
		@Override
		public void onInfoWindowClick(Marker marker)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(MapView.this);
			dialog.setTitle(getString(R.string.map_dialog_title));
			dialog.setMessage(getString(R.string.map_dialog_message) + "\n" + message2);
			dialog.setNegativeButton(getString(R.string.map_dialog_button_negative), new MyDialogOnNegativeClickListener());
			dialog.setPositiveButton(getString(R.string.map_dialog_button_positive), new MyDialogOnPositiveClickListener());
			dialog.create().show();
		}
	}

	public class MyDialogOnPositiveClickListener implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if (latLng != null)
			{
				try
				{
					GPSConfig.address = message2;
					GPSConfig.setLocation(MapView.this, latLng);

					Toast.makeText(MapView.this, "Adres succesvol ingesteld", Toast.LENGTH_LONG).show();
					finish();
				}
				catch (IOException e)
				{
					Toast.makeText(MapView.this, "Adres kon niet worden opgeslagen", Toast.LENGTH_LONG).show();
					finish();
					e.printStackTrace();
				}
			}
			else
			{
				Toast.makeText(MapView.this, "Geen idee waar", Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}

	public class MyDialogOnNegativeClickListener implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			dialog.dismiss();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_view, menu);
		return true;
	}

	
	public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting())
		{
			return true;
		}
		return false;
	}
}
