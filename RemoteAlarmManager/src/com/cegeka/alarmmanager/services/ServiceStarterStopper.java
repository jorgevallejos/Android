package com.cegeka.alarmmanager.services;

import android.content.Context;
import android.content.Intent;

public class ServiceStarterStopper {

	
	public static void startSyncService(Context ctx){
		Intent alarmIntent = new Intent(ctx, SyncService.class);
		ctx.startService(alarmIntent);
	}
	
	public static void stopSyncService(Context ctx){
		Intent alarmIntent = new Intent(ctx, SyncService.class);
		ctx.stopService(alarmIntent);
	}
}
