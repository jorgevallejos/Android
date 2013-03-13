package be.cegeka.android.dwaaldetectie.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class AutoStart extends BroadcastReceiver {

	public void onReceive(Context ctx, Intent arg1) 
	{
		Intent intent = new Intent(ctx, LocationService.class);
		ctx.startService(intent);
	}
}