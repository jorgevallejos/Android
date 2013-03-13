package be.cegeka.android.dwaaldetectie.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.model.ApplicationLogic;
import com.example.dwaaldetectie.R;


public class Settings extends Activity
{
	public void handleCancel(View view)
	{
		Intent intent = new Intent();
		setResult(RESULT_CANCELED, intent);
		finish();
	}
	
	
	public void handleSave(View view)
	{
//		Object object = findViewById(R.id.editText1);
//		System.out.println(object.getClass());
		Object object = findViewById(R.id.editText2);
		System.out.println(object.getClass());
//		
//		String locatie = address.getText() + ", " + plaats.getText();
//		ApplicationLogic.setLocation(locatie);
//		
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();
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
}
