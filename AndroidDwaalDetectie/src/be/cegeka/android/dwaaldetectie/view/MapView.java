package be.cegeka.android.dwaaldetectie.view;

import java.io.IOException;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.R;
import be.cegeka.android.dwaaldetectie.model.AddressConverter;
import be.cegeka.android.dwaaldetectie.model.DavidsAdapter;
import be.cegeka.android.dwaaldetectie.model.GPSConfig;
import be.cegeka.android.dwaaldetectie.model.SuggestionsTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
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
	public DavidsAdapter adapter;
	public AutoCompleteTextView textView;
	private SuggestionsTask suggestionsTask;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		adapter = new DavidsAdapter(this, android.R.layout.simple_list_item_1);
		adapter.setNotifyOnChange(true);
		suggestionsTask = new SuggestionsTask();
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
			map.setOnMapClickListener(new MyMapClickListener());
			map.setOnMapLongClickListener(new MyMapLongClickListener());
			map.setOnInfoWindowClickListener(new MyInfoWindowClickListener());
		}
	}


	private void createDialog()
	{
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MapView.this);
		dialogBuilder.setTitle(getString(R.string.map_dialog_search_title));

		textView = new AutoCompleteTextView(MapView.this);
		textView.setThreshold(3);
		textView.setInputType(InputType.TYPE_CLASS_TEXT);
		textView.setAdapter(adapter);
		textView.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				suggestionsTask = new SuggestionsTask();
				suggestionsTask.execute(MapView.this);
			}


			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}


			@Override
			public void afterTextChanged(Editable s)
			{
			}
		});

		dialogBuilder.setView(textView);
		textView.setPadding(50, 20, 50, 20);

		dialogBuilder.setPositiveButton(getString(R.string.map_dialog_search_button_positive), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (!isOnline())
				{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapView.this);
					alertDialog.setTitle(getString(R.string.map_info_no_internet_title));
					alertDialog.setMessage(getString(R.string.map_info_no_internet));
					alertDialog.setPositiveButton(getString(R.string.map_dialog_not_found_button), new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
							createDialog();
						}
					});
					alertDialog.create().show();
				}
				else
				{
					try
					{
						Geocoder geocoder = new Geocoder(MapView.this);
						List<Address> addresses = geocoder.getFromLocationName(textView.getText().toString(), 1);

						if (!addresses.isEmpty())
						{
							Address address = addresses.get(0);
							message = AddressConverter.convertAddress(address);
							message2 = message;
							latLng = new LatLng(address.getLatitude(), address.getLongitude());

							map.clear();

							MarkerOptions markerOptions = new MarkerOptions();
							markerOptions.position(latLng);
							markerOptions.title(message);
							markerOptions.draggable(false);
							Marker marker = map.addMarker(markerOptions);
							marker.showInfoWindow();

							CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
							map.animateCamera(cameraUpdate);
						}
						else
						{
							AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapView.this);
							alertDialog.setTitle(getString(R.string.map_dialog_not_found_title));
							alertDialog.setMessage(getString(R.string.map_dialog_not_found_message));
							alertDialog.setPositiveButton(getString(R.string.map_dialog_not_found_button), new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.dismiss();
									createDialog();
								}
							});
							alertDialog.create().show();
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		});
		dialogBuilder.setNegativeButton(getString(R.string.map_dialog_search_button_negative), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		alertDialog.show();
	}

	public class MyMapClickListener implements OnMapClickListener
	{
		@Override
		public void onMapClick(LatLng arg0)
		{
			createDialog();
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
						message = AddressConverter.convertAddress(address);
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

					Toast.makeText(MapView.this, getString(R.string.map_address_success), Toast.LENGTH_LONG).show();
					finish();
				}
				catch (IOException e)
				{
					Toast.makeText(MapView.this, getString(R.string.map_address_fail), Toast.LENGTH_LONG).show();
					finish();
					e.printStackTrace();
				}
			}
			else
			{
				Toast.makeText(MapView.this, getString(R.string.map_address_fail), Toast.LENGTH_LONG).show();
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
