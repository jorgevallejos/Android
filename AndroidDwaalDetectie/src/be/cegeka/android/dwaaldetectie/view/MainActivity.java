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


public class MainActivity extends Activity
{

	private ToggleButton startButton;
	private static TextView textView;


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
		GPSConfig.interfaceUp = true;
		initHandlers();
		updateDistance();
	}
	

	@Override
	protected void onDestroy()
	{
		GPSConfig.interfaceUp = false;
		super.onDestroy();
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
					int result = GPSConfig.initialiseApp(MainActivity.this);
					if(result == GPSConfig.RESULT_NO_ADDRESS_SET)
					{
						startButton.setChecked(false);
						handleShowMap(null);
					}
					else if(result == GPSConfig.RESULT_OK)
					{
						updateDistance();
					}
					else
					{
						Toast.makeText(MainActivity.this, R.string.error_unable_to_start, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public void handleShowMap(View view)
	{
		if (!isOnline())
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
		editText.setPadding(50, 20, 50, 20);
		alertDialogBuilder.setTitle(R.string.main_dialogMaxDistance_title);
		alertDialogBuilder.setMessage(R.string.main_dialogMaxDistance_message);
		alertDialogBuilder.setView(editText);
		alertDialogBuilder.setNegativeButton(getString(R.string.button_cancel), new AlertDialog.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		alertDialogBuilder.setPositiveButton(getString(R.string.button_save), new AlertDialog.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String text = editText.getText().toString();
				try
				{
					long distance = Long.parseLong(text);
					GPSConfig.maxDistance = distance;
					AddressLoaderSaver.saveMaxDistance(MainActivity.this, distance);
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
				}
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
