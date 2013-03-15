package be.cegeka.android.dwaaldetectie.model;

import com.google.android.gms.maps.model.LatLng;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;


public class AutoStart extends BroadcastReceiver
{

	@SuppressLint("ShowToast")
	public void onReceive(Context ctx, Intent arg1)
	{
		try
		{
			LatLng latLng = AddressLoaderSaver.loadAddress(ctx);
			
			GPSConfig.setLocation(ctx, latLng);
			
			GPSConfig.changeListener = new LocationChangeListener(ctx);
			ctx.startService(new Intent(ctx, GPSService.class));
		}
		catch (Exception e)
		{
			Toast.makeText(ctx, "DwaalDetectie kon niet worden gestart", Toast.LENGTH_LONG);
		}
	}
}