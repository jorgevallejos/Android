package be.cegeka.android.dwaaldetectie.model;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

public class AutoStart extends BroadcastReceiver {

	@SuppressLint("ShowToast")
	public void onReceive(Context ctx, Intent arg1) 
	{
		try{
			ApplicationLogic applicationLogic = new ApplicationLogic(ctx);
			String locatie = AddressLoaderSaver.loadAddress();
			Location location = applicationLogic.locationFromAddress(locatie);
			GPSConfig.location = location;
			GPSConfig.changeListener = new LocationChangeListener(ctx);
			ctx.startService(new Intent(ctx, GPSService.class));

		}catch(Exception e){
			Toast.makeText(ctx, "DwaalDetectie kon niet worden gestart", Toast.LENGTH_LONG);
		}
	}
}