package com.cegeka.alarmmanager.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStart extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent alarmIntent = new Intent(context, AlarmService.class);
		context.startService(alarmIntent);
	}

}
