package be.cegeka.android.dwaaldetectie.view;

import static be.cegeka.android.dwaaldetectie.model.GPSConfig.getGPSConfig;
import java.io.IOException;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.R;
import be.cegeka.android.dwaaldetectie.model.AddressSuggestionListAdapter;
import be.cegeka.android.dwaaldetectie.model.SuggestionsTask;
import be.cegeka.android.dwaaldetectie.utilities.AddressConverter;
import be.cegeka.android.dwaaldetectie.utilities.NetworkCheck;
import be.cegeka.android.dwaaldetectie.view.listeners.DialogDismissListener;
import be.cegeka.android.dwaaldetectie.view.listeners.TextWatcherAdapter;
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
	private NetworkCheck networkCheck;
	private LatLng latLng;
	private GoogleMap map;
	private String tempAddressDescription;
	private String addressDescription;
	private AddressSuggestionListAdapter adapter;
	private AutoCompleteTextView textView;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);

		initFields();
		initMap();
	}


	private void initFields()
	{
		networkCheck = new NetworkCheck(this);

		adapter = new AddressSuggestionListAdapter(this, android.R.layout.simple_list_item_1);
		adapter.setNotifyOnChange(true);
	}


	private void initMap()
	{
		if (map == null)
		{
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		}
		map.setMyLocationEnabled(true);
		map.setOnMapClickListener(new MyMapClickListener());
		map.setOnMapLongClickListener(new MyMapLongClickListener());
		map.setOnInfoWindowClickListener(new MyInfoWindowClickListener());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.map_view, menu);
		return true;
	}


	public AddressSuggestionListAdapter getAdapter()
	{
		return adapter;
	}


	private void createAddressSearchDialog()
	{
		textView = new AutoCompleteTextView(MapView.this);
		textView.setThreshold(3);
		textView.setInputType(InputType.TYPE_CLASS_TEXT);
		textView.setAdapter(adapter);
		textView.setPadding(50, 20, 50, 20);
		textView.addTextChangedListener(new AddressTextWatcher());

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MapView.this)
				.setTitle(getString(R.string.map_dialog_search_title))
				.setView(textView)
				.setPositiveButton(getString(R.string.map_dialog_search_button_positive), new SearchForAddressClickListener())
				.setNegativeButton(getString(R.string.map_dialog_search_button_negative), new DialogDismissListener());

		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		alertDialog.show();
	}


	private void createErrorDialog(String title, String message, String buttonText)
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapView.this);
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setPositiveButton(buttonText, new DialogDismissListener());
		alertDialog.create().show();
	}


	private void searchForAddress()
	{
		try
		{
			Geocoder geocoder = new Geocoder(MapView.this);
			List<Address> addresses = geocoder.getFromLocationName(textView.getText().toString(), 1);

			if (!addresses.isEmpty())
			{
				Address address = addresses.get(0);
				tempAddressDescription = AddressConverter.convertAddress(address);
				addressDescription = tempAddressDescription;
				latLng = new LatLng(address.getLatitude(), address.getLongitude());

				map.clear();
				showLatLngOnMap(latLng, tempAddressDescription);

				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
				map.animateCamera(cameraUpdate);
			}
			else
			{
				createErrorDialog(getString(R.string.map_dialog_not_found_title), getString(R.string.map_dialog_not_found_message), getString(R.string.map_dialog_not_found_button));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	private void searchAddressForPoint(LatLng point)
	{
		latLng = point;

		try
		{
			Geocoder geocoder = new Geocoder(MapView.this);
			List<Address> addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
			if (!addresses.isEmpty())
			{
				Address address = addresses.get(0);
				tempAddressDescription = AddressConverter.convertAddress(address);
				addressDescription = tempAddressDescription;
			}
			else
			{
				tempAddressDescription = getString(R.string.map_marker_message_no_address_found);
				addressDescription = latLng.latitude + ", " + latLng.longitude;
			}

			map.clear();
			showLatLngOnMap(point, tempAddressDescription);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	private void showLatLngOnMap(LatLng point, String message)
	{
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(point);
		markerOptions.title(message);
		markerOptions.draggable(false);
		Marker marker = map.addMarker(markerOptions);
		marker.showInfoWindow();
	}


	private class MyMapClickListener implements OnMapClickListener
	{
		@Override
		public void onMapClick(LatLng arg0)
		{
			createAddressSearchDialog();
		}
	}


	private class MyMapLongClickListener implements OnMapLongClickListener
	{
		@Override
		public void onMapLongClick(LatLng point)
		{
			if (!networkCheck.isOnline())
			{
				Toast.makeText(MapView.this, R.string.map_info_no_internet, Toast.LENGTH_SHORT).show();
			}
			else
			{
				searchAddressForPoint(point);
			}
		}
	}


	private class AddressTextWatcher extends TextWatcherAdapter
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			new SuggestionsTask(textView.getText().toString()).execute(MapView.this);
		}
	}


	private class SearchForAddressClickListener implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if (!networkCheck.isOnline())
			{
				createErrorDialog(getString(R.string.map_info_no_internet_title), getString(R.string.map_info_no_internet), getString(R.string.map_dialog_not_found_button));
			}
			else
			{
				searchForAddress();
			}
		}
	}


	private class MyInfoWindowClickListener implements OnInfoWindowClickListener
	{
		@Override
		public void onInfoWindowClick(Marker marker)
		{
			AlertDialog.Builder dialog = new AlertDialog.Builder(MapView.this)
					.setTitle(getString(R.string.map_dialog_title))
					.setMessage(getString(R.string.map_dialog_message) + "\n" + addressDescription)
					.setNegativeButton(getString(R.string.map_dialog_button_negative), new DialogNegativeClickListener())
					.setPositiveButton(getString(R.string.map_dialog_button_positive), new NewAddressClickListener());

			dialog.create().show();
		}
	}


	private class NewAddressClickListener implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			if (latLng != null)
			{
				try
				{
					getGPSConfig().setAddress(addressDescription);
					getGPSConfig().setLocation(MapView.this, latLng);

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


	private class DialogNegativeClickListener implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			dialog.dismiss();
		}
	}
}
