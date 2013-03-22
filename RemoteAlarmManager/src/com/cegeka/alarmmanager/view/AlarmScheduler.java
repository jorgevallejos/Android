package com.cegeka.alarmmanager.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.cegeka.alarmmanager.db.Alarm;

public class AlarmScheduler {
	
	public static HashMap<Long, PendingIntent> alarmIntents = new HashMap<Long,PendingIntent>();
 	public static void scheduleAlarm(Context context, Alarm alarm) {
		// Create intent
		Intent intent = new Intent(context, AlarmReceiverActivity.class);
		// Add data to intent
		intent.putExtra("Alarm", alarm);
		// Create pending intent
		PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
		alarmIntents.put(alarm.getId(), pendingIntent);
		// Get Alarm service and add pending intent
		AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, alarm.getDate().getTimeInMillis(), pendingIntent);
	}
	
	
	public static void cancelAlarms(Context context, ArrayList<Alarm> alarmen){
		AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
		for(Alarm a : alarmen){
			Intent intent = new Intent(context, AlarmReceiverActivity.class);
			PendingIntent pintent = PendingIntent.getActivity(context, (int) a.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
			if(pintent!=null){
				System.out.println("ALARM CANCELLED");
				am.cancel(pintent);
				alarmIntents.remove(a.getId());
			}
		}
	}
}
