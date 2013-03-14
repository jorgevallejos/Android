package be.cegeka.android.dwaaldetectie.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.model.ApplicationLogic;
import com.example.dwaaldetectie.R;


public class Settings extends Activity
{
	private List<Address> suggestions;
	ArrayAdapter<String> adapter;
	AutoCompleteTextView autoCompleteTextView;
	Geocoder geocoder;


	public void handleCancel(View view)
	{
		Intent intent = new Intent();
		setResult(RESULT_CANCELED, intent);
		finish();
	}


	public void handleSave(View view)
	{
		EditText address = (EditText) findViewById(R.id.editText111);
		EditText place = (EditText) findViewById(R.id.editText2);

		if (address.getText().length() == 0 || place.getText().length() == 0)
		{
			Toast.makeText(this, R.string.toast_invalid_input, Toast.LENGTH_LONG).show();
		}
		else
		{
			ApplicationLogic applicationLogic = new ApplicationLogic(this);
			String locatie = address.getText() + ", " + place.getText();
			Location location = applicationLogic.locationFromAddress(locatie);
			ApplicationLogic.location = location;

			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item);
		adapter.setNotifyOnChange(true);

		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		autoCompleteTextView.setAdapter(adapter);
		autoCompleteTextView.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if (count % 3 == 1)
				{
					adapter.clear();
					
					Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
					System.out.println(autoCompleteTextView.getText().toString());
					try
					{
						List<Address> addresses = geocoder.getFromLocationName(autoCompleteTextView.getText().toString(), 1);
						
						if(!addresses.isEmpty())
						{
							adapter.add(addresses.get(0).toString());
							adapter.notifyDataSetChanged();
						}
						else
						{
							adapter.notifyDataSetInvalidated();
						}
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}


			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				// TODO Auto-generated method stub

			}


			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub

			}
		});
	}


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		/**
		 * Locatie verspringt naar standaard indien back toets van Android
		 * gebruikt wordt. Dit is geen oplossing.
		 */

		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			finish();
			return true;
		}
		else
		{
			return super.onKeyUp(keyCode, event);
		}
	}

}
