package be.cegeka.android.dwaaldetectie.view;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.R;
import be.cegeka.android.dwaaldetectie.model.AddressLoaderSaver;
import be.cegeka.android.dwaaldetectie.model.GPSConfig;



public class Settings extends Activity
{
	private AutoCompleteTextView autoCompleteTextView;
	ArrayAdapter<String> adapter;


	public void handleCancel(View view)
	{
		Intent intent = new Intent();
		setResult(RESULT_CANCELED, intent);
		finish();
	}


	public void handleSave(View view)
	{
		AutoCompleteTextView address = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

		if (address.getText().length() == 0)
		{
			Toast.makeText(this, R.string.toast_invalid_input, Toast.LENGTH_LONG).show();
		}
		else
		{
//			ApplicationLogic applicationLogic = new ApplicationLogic(this);
//			Location location = applicationLogic.locationFromAddress(address.getText().toString());
//			GPSConfig.location = location;

//			try
//			{
//				AddressLoaderSaver.saveAddress(this, address.getText().toString());
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}

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


		adapter = new ArrayAdapter<String>(this, R.layout.list_item);
		adapter.setNotifyOnChange(true);
		
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		autoCompleteTextView.setAdapter(adapter);
		
		autoCompleteTextView.addTextChangedListener(new TextWatcher()
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				adapter.clear();
				Geocoder geocoder = new Geocoder(Settings.this.getApplicationContext(), Locale.getDefault());
				try
				{
					List<Address> addresses = geocoder.getFromLocationName(autoCompleteTextView.getText().toString(), 5);
					if(!addresses.isEmpty())
					{
						for(int i = 0; i < addresses.size(); i++)
						{
							adapter.add(addresses.get(i).getAddressLine(0) + ", " + addresses.get(i).getAddressLine(1) + ", " + addresses.get(i).getAddressLine(2));
						}
					}
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
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
