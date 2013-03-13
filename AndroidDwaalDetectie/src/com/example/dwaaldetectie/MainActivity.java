package com.example.dwaaldetectie;

import java.util.List;
import java.util.Locale;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private double longitude;
	private double latitude;
	private Location baseLocation;
	private TextView view;
	private boolean first=false;
	private final LocationListener locationListener = new LocationListener() {
		
		public void onLocationChanged(Location location) {
			
			// Get Longitude and latitude from current location.
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			
			// Print to console
			System.out.println("Longitude Current Address: " + longitude);
			System.out.println("Latitude Current Address: " + latitude);
			
			//test test test test rr
			
			addresFromLocation(location);
			System.out.println(baseLocation.distanceTo(location));
			
			if(baseLocation!=null && baseLocation.distanceTo(location) > 2000){
				Toast.makeText(MainActivity.this, "Too far", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override 
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = (TextView) findViewById(R.id.locationTextview);
		getFromLocation("gelijkheidstraat 6, Leuven");
		setContentView(R.layout.activity_main);
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void addresFromLocation(Location location){
		try{
			Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
			List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (addresses.isEmpty()) {
				view.setText("Waiting for Location");
			}
			else {
				if (addresses.size() > 0) {
					view.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace(); // getFromLocation() may sometimes fail
		}
	}
	private void getFromLocation(String address)
	{
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());    
		try 
		{
			List<Address> addresses = geoCoder.getFromLocationName(address , 1);
			if (addresses.size() > 0) 
			{            
				baseLocation = new Location("GPS");
				Address a = addresses.get(0);
				System.out.println("Longitude Base Address: " + a.getLongitude());
				System.out.println("Latitude Base Address: " + a.getLatitude());
				baseLocation.setLatitude(addresses.get(0).getLatitude());
				baseLocation.setLongitude(addresses.get(0).getLongitude());
				latitude=baseLocation.getLatitude();
				longitude=baseLocation.getLongitude();
			}
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}



}
