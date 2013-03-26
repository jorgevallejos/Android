package com.cegeka.alarmmanager.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver{

	/**
	 * Auto start the alarmservice on start-up
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent alarmIntent = new Intent(context, AlarmService.class);
		context.startService(alarmIntent);
	}

}
