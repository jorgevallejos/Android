package be.cegeka.android.dwaaldetectie.view;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import be.cegeka.android.dwaaldetectie.model.ApplicationLogic;
import be.cegeka.android.dwaaldetectie.model.LocationChangeListener;
import com.example.dwaaldetectie.R;


public class MainActivity extends Activity
{
	ApplicationLogic applicationLogic;
	private Location baseLocation;
	private TextView view;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		view = (TextView) findViewById(R.id.locationTextview);
		setContentView(R.layout.activity_main);
		applicationLogic = new ApplicationLogic(this);
		
		baseLocation = applicationLogic.locationFromAddress("interleuvenlaan 1, heverlee");
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


	

}
