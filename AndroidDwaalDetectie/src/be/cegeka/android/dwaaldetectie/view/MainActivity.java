package be.cegeka.android.dwaaldetectie.view;

import static be.cegeka.android.dwaaldetectie.model.TrackingConfiguration.trackingConfig;
import java.util.Observable;
import java.util.Observer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import be.cegeka.android.dwaaldetectie.model.TrackingService;
import be.cegeka.android.dwaaldetectie.utilities.NetworkCheck;
import be.cegeka.android.dwaaldetectie.view.listeners.DialogDismissListener;


/**
 * Activity which is opened when the app is launched, this is the startscreen.
 * It holds several buttons and displays home address and distance from home
 * address when Service is enabled.
 */
public class MainActivity extends Activity implements Observer
{
	private NetworkCheck networkCheck;
	private ToggleButton startButton;
	private TextView textView;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initFields();
		initListeners();
		updateDistance();

		trackingConfig().addObserver(this);
	}


	/**
	 * Initialises instance variables.
	 */
	private void initFields()
	{
		networkCheck = new NetworkCheck(this);
		textView = (TextView) findViewById(R.id.textView1);
		startButton = (ToggleButton) findViewById(R.id.startButton);
		if (TrackingService.isRunning())
		{
			startButton.setChecked(true);
		}
	}


	/**
	 * Adds all Listeners.
	 */
	private void initListeners()
	{
		startButton.setOnClickListener(new StartButtonListener());
	}


	/**
	 * Is called when the users clicks the "configure address" button, changes
	 * Activity to the MapView.
	 * 
	 * @param view
	 */
	public void handleShowMap(View view)
	{
		if (!networkCheck.isOnline())
		{
			Toast.makeText(this, R.string.map_info_no_internet, Toast.LENGTH_SHORT).show();
		}
		else
		{
			Intent intent = new Intent(this, MapView.class);
			startActivity(intent);
		}
	}


	/**
	 * Is called when the user clicks the "set max distance" button, shows a
	 * dialog in which the maximum distance can be set and saved.
	 * 
	 * @param view
	 */
	public void handleMaxDistance(View view)
	{
		EditText editText = new EditText(this);
		editText.setInputType(InputType.TYPE_CLASS_NUMBER);
		editText.setPadding(50, 20, 50, 20);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this)
				.setTitle(R.string.main_dialogMaxDistance_title)
				.setMessage(R.string.main_dialogMaxDistance_message)
				.setView(editText)
				.setNegativeButton(getString(R.string.button_cancel), new DialogDismissListener())
				.setPositiveButton(getString(R.string.button_save), new SaveMaxDistanceListener(editText));

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		alertDialog.show();
	}


	/**
	 * Updates the textView that shows the home address and distance from the
	 * home address.
	 */
	private void updateDistance()
	{
		if (trackingConfig().getAddress() != null)
		{
			textView.setText(trackingConfig().getAddress() + "\n\n" + trackingConfig().getDistance());
		}
		else
		{
			textView.setText(trackingConfig().getDistance());
		}
	}


	@Override
	public void update(Observable observable, Object data)
	{
		updateDistance();
	}


	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	/**
	 * OnClickListener for the start button. Starts the Service if the button
	 * gets enabled and a home address is set, stops the Service if the button
	 * gets disabled. Sends the user to the map if no address is set.
	 */
	private class StartButtonListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (!startButton.isChecked())
			{
				stopService(new Intent(MainActivity.this, TrackingService.class));
				textView.setText("");
			}
			else
			{
				int result = trackingConfig().startTrackingService(MainActivity.this);
				if (result == trackingConfig().RESULT_NO_ADDRESS_SET)
				{
					startButton.setChecked(false);
					handleShowMap(null);
				}
				else if (result == trackingConfig().RESULT_OK)
				{
					updateDistance();
				}
				else
				{
					Toast.makeText(MainActivity.this, R.string.error_unable_to_start, Toast.LENGTH_LONG).show();
				}
			}
		}
	}


	/**
	 * OnClickListener for the max distance dialog. Parses the input to a long
	 * and sets and saves this input. The EditText in which the max distance is
	 * typed needs to be passed in the Constructor.
	 */
	private class SaveMaxDistanceListener implements AlertDialog.OnClickListener
	{
		private final EditText editText;


		private SaveMaxDistanceListener(EditText editText)
		{
			this.editText = editText;
		}


		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			String text = editText.getText().toString();
			try
			{
				long distance = Long.parseLong(text);
				trackingConfig().setMaxDistance(distance);
				AddressLoaderSaver.saveMaxDistance(MainActivity.this, distance);
			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
	}
}
