package com.cegeka.alarmtest.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.cegeka.alarmtest.db.Alarm;
import com.cegeka.alarmtest.db.AlarmsDataSource;

public class AlarmScheduler {
	
	public static Context context;


	public static void scheduleAlarm(Alarm alarm) {
		
		AlarmsDataSource alarmDS = new AlarmsDataSource(context);
		alarmDS.open();
		alarm = alarmDS.createAlarm(alarm);
		// Create intent
		Intent intent = new Intent(context, AlarmReceiverActivity.class);
		// Add data to intent
		intent.putExtra("Alarm", alarm);
		System.out.println(alarm.toString());
		// Create pending intent
		PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
		// Get Alarm service and add pending intent
		AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, alarm.getDate().getTimeInMillis(), pendingIntent);
		alarmDS.close();
	}


}
