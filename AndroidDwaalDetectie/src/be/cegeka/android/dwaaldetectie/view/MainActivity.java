package be.cegeka.android.dwaaldetectie.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
						handleShowMap(null);
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


	public void handleShowMap(View view)
	{
		if(!isOnline())
		{
			Toast.makeText(this, R.string.map_info_no_internet, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Intent intent = new Intent(this, MapView.class);
			startActivity(intent);
		}
	}
	
	
	public void handleMaxDistance(View view)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		final EditText editText = new EditText(this);
		editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		alertDialogBuilder.setTitle(R.string.main_dialogMaxDistance_title);
		alertDialogBuilder.setMessage(R.string.main_dialogMaxDistance_message);
		alertDialogBuilder.setView(editText);
		alertDialogBuilder.setNegativeButton("Cancel", new AlertDialog.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				finish();
			}
		});
		alertDialogBuilder.setPositiveButton("Ok", new AlertDialog.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String text = editText.getText().toString();
				int distance = Integer.parseInt(text);
				GPSConfig.maxDistance = distance;
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		alertDialog.show();
	}


	public static void updateDistance()
	{
		if (GPSConfig.address != null)
		{
			textView.setText(GPSConfig.address + "\n\n" + GPSConfig.getDistance());
		}
		else
		{
			textView.setText(GPSConfig.getDistance());
		}
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
