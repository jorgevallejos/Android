package be.cegeka.android.dwaaldetectie.view;

import java.io.IOException;
import java.util.ArrayList;
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
import android.widget.EditText;
import be.cegeka.android.dwaaldetectie.model.AddressLoaderSaver;
import be.cegeka.android.dwaaldetectie.model.GPSConfig;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.model.ApplicationLogic;
import com.example.dwaaldetectie.R;

public class Settings extends Activity
{
	private List<Address> suggestions;
	ArrayAdapter<Address> adapter;


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
			GPSConfig.location = location;
			
			try {
				AddressLoaderSaver.saveAddress(locatie);
			} catch (IOException e) {
				e.printStackTrace();
			}

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

//		suggestions = new ArrayList<String>();
//		suggestions.add("Domstraat 15, Willebringen");
//		suggestions.add("Interleuvenlaan 3, Heverlee");
//		AutoCompleteTextView autoCompleteText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
//		
//		Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
//		try
//		{
//			suggestions = geo.getFromLocationName(autoCompleteText.getText().toString(), 3);
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	    adapter = new ArrayAdapter<Address>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
//
//		
//		autoCompleteText.setAdapter(adapter);
//		
//		autoCompleteText.addTextChangedListener(new TextWatcher()
//		{
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count)
//			{
//				// TODO Auto-generated method stub
//				
//			}
//			
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count, int after)
//			{
//				// TODO Auto-generated method stub
//				
//			}
//			
//			
//			@Override
//			public void afterTextChanged(Editable s)
//			{
//				Geocoder geo = new Geocoder(getParent().getApplicationContext(), Locale.getDefault());
//				try
//				{
//					suggestions = geo.getFromLocationName(((AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1)).getText().toString(), 3);
//				}
//				catch (IOException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
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
