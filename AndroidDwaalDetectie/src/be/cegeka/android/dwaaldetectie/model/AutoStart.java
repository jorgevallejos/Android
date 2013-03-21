package be.cegeka.android.dwaaldetectie.model;

import static be.cegeka.android.dwaaldetectie.model.GPSConfig.getGPSConfig;
import be.cegeka.android.dwaaldetectie.R;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class AutoStart extends BroadcastReceiver
{
	@SuppressLint("ShowToast")
	public void onReceive(Context ctx, Intent arg1)
	{
		int result = getGPSConfig().startTrackingService(ctx);

		if (result == getGPSConfig().RESULT_ERROR || result == getGPSConfig().RESULT_NO_ADDRESS_SET)
		{
			Toast.makeText(ctx, R.string.error_unable_to_start, Toast.LENGTH_LONG);
		}
	}
}