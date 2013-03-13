package be.cegeka.android.dwaaldetectie.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.model.ApplicationLogic;
import be.cegeka.android.dwaaldetectie.model.LocationChangeListener;
import com.example.dwaaldetectie.R;


public class MainActivity extends Activity
{
	ApplicationLogic applicationLogic;
	private Location baseLocation;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		applicationLogic = new ApplicationLogic(this);
		
		String locationString = ApplicationLogic.getLocation();
		baseLocation = applicationLogic.locationFromAddress(locationString);
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationChangeListener(this, baseLocation));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public void handleSettings(View view)
	{
		Intent intent = new Intent(this, Settings.class);
		startActivityForResult(intent, 10);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK && requestCode == 10)
		{
			String locationString = ApplicationLogic.getLocation();
			baseLocation = applicationLogic.locationFromAddress(locationString);
			Toast.makeText(this, R.string.toast_address_saved, Toast.LENGTH_LONG).show();
		}
	}
}

