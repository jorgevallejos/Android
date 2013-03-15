package be.cegeka.android.dwaaldetectie.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import be.cegeka.android.dwaaldetectie.R;
import be.cegeka.android.dwaaldetectie.model.AddressLoaderSaver;
import be.cegeka.android.dwaaldetectie.model.GPSConfig;
import be.cegeka.android.dwaaldetectie.model.GPSService;
import be.cegeka.android.dwaaldetectie.model.LocationChangeListener;
import com.google.android.gms.maps.model.LatLng;


public class MainActivity extends Activity
{

	private ToggleButton startButton;
	private static TextView textView;
	public static boolean interfaceup;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startButton = (ToggleButton) findViewById(R.id.startButton);
		textView = (TextView) findViewById(R.id.textView1);
		if (GPSConfig.changeListener == null)
		{
			GPSConfig.changeListener = new LocationChangeListener(this);
		}
		if (GPSService.running)
		{
			startButton.setChecked(true);
		}
		interfaceup = true;
		initHandlers();
		updateDistance();
	}


	private void initHandlers()
	{
		startButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (!startButton.isChecked())
				{
					stopService(new Intent(MainActivity.this, GPSService.class));
					textView.setText("");
				}
				else
				{
					try
					{
						String address = AddressLoaderSaver.loadAddressDescription(MainActivity.this);
						LatLng latLng = AddressLoaderSaver.loadAddress(MainActivity.this);
						GPSConfig.address = address;
						GPSConfig.setLocation(MainActivity.this, latLng);
						startService(new Intent(MainActivity.this, GPSService.class));
						updateDistance();
					}
					catch (Exception e)
					{
						e.printStackTrace();
						startButton.setChecked(false);
						Intent intent = new Intent(MainActivity.this, MapView.class);
						startActivityForResult(intent, 10);
					}
				}
			}
		});
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
		Intent intent = new Intent(this, MapView.class);
		startActivity(intent);
	}


	public void handleShowMap(View view)
	{
		Intent intent = new Intent(this, MapView.class);
		startActivity(intent);
	}


	public static void updateDistance()
	{
		if(GPSConfig.address != null)
		{
			textView.setText(GPSConfig.address + "\n\n" + GPSConfig.getDistance());
		}
		else
		{
			textView.setText(GPSConfig.getDistance());
		}
	}
}
