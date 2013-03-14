package be.cegeka.android.dwaaldetectie.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import be.cegeka.android.dwaaldetectie.view.MainActivity;

public class LocationService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStart(Intent intent, int startid)
	{
		Intent intents = new Intent(getBaseContext(),MainActivity.class);
		intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intents);
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
	}

}
