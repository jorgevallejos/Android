package be.cegeka.android.dwaaldetectie.view;

import java.text.DecimalFormat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import be.cegeka.android.dwaaldetectie.model.AddressLoaderSaver;
import be.cegeka.android.dwaaldetectie.model.ApplicationLogic;
import be.cegeka.android.dwaaldetectie.model.GPSConfig;
import be.cegeka.android.dwaaldetectie.model.GPSService;
import be.cegeka.android.dwaaldetectie.model.LocationChangeListener;
import com.example.dwaaldetectie.R;


public class MainActivity extends Activity
{
	private ToggleButton startButton;
	private boolean isloaded;
	private static TextView textView;
	public static boolean interfaceup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startButton = (ToggleButton) findViewById(R.id.startButton);
		textView = (TextView) findViewById(R.id.textView1);
		if(GPSConfig.changeListener==null){
			GPSConfig.changeListener = new LocationChangeListener(this);
		}
		if(GPSService.running){
			startButton.setChecked(true);
		}
		interfaceup=true;
		initHandlers();
	}



	private void initHandlers(){

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!startButton.isChecked()){
					stopService(new Intent(MainActivity.this, GPSService.class));
				}else{
					try{
						ApplicationLogic applicationLogic = new ApplicationLogic(MainActivity.this);
						String locatie = AddressLoaderSaver.loadAddress();
						Location location = applicationLogic.locationFromAddress(locatie);
						GPSConfig.location = location;
						startService(new Intent(MainActivity.this, GPSService.class));
						
					}catch(Exception e){
						e.printStackTrace();
						startButton.setChecked(false);
						Intent intent = new Intent(MainActivity.this, Settings.class);
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
		Intent intent = new Intent(this, Settings.class);
		startActivityForResult(intent, 10);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			Toast.makeText(this, R.string.toast_address_saved, Toast.LENGTH_LONG).show();
		}
	}


	public static void updateDistance(float distance)
	{
		String result = null;

		if (distance < 1000)
		{
			DecimalFormat decimalFormat = new DecimalFormat("#");
			result = decimalFormat.format(distance) + " m";
		}
		else
		{
			DecimalFormat decimalFormat = new DecimalFormat("#.#");
			distance = distance / 1000;
			result = decimalFormat.format(distance) + " km";
		}

		
		textView.setText("" + result);
	}
}
