package com.cegeka.alarmmanager.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.cegeka.alarmmanager.db.Alarm;

public class AlarmScheduler {

	public static void scheduleAlarm(Context context, Alarm alarm) {
		// Create intent
		Intent intent = new Intent(context, AlarmReceiverActivity.class);
		// Add data to intent
		intent.putExtra("Alarm", alarm);
		// Create pending intent
		PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
		// Get Alarm service and add pending intent
		AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, alarm.getDate().getTimeInMillis(), pendingIntent);
	}
}
