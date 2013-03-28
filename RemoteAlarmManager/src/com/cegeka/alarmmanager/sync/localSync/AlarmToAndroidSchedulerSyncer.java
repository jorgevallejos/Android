package com.cegeka.alarmmanager.sync.localSync;

import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.cegeka.alarmmanager.model.Alarm;
import com.cegeka.alarmmanager.view.AlarmReceiverActivity;

public class AlarmToAndroidSchedulerSyncer {

	/**
	 * schedule an {@link Alarm} in the {@link AlarmManager}.
	 * 
	 * @param context
	 *            The context
	 * @param alarm
	 *            The {@link Alarm} to be scheduled.
	 */
	public static void scheduleAlarms(Context context, List<Alarm> alarmen) {
		for (Alarm alarm : alarmen) {
			scheduleAlarm(context, alarm);
		}

	}

	/**
	 * Cancel the given Alarms from the {@link AlarmManager}.
	 * 
	 * @param context
	 *            The context.
	 * @param alarmen
	 *            The alarms to cancel.
	 */
	public static void cancelAlarms(Context context, List<Alarm> alarmen) {
		for (Alarm a : alarmen) {
			cancelAlarm(context, a);
		}
	}

	private static void cancelAlarm(Context context, Alarm alarm) {
		PendingIntent pintent = createPendingIntentFor(context, alarm);
		boolean wasScheduledBefore = pintent != null;
		if (wasScheduledBefore) {
			alarmManager(context).cancel(pintent);
		}
	}


	public static void scheduleAlarm(Context context, Alarm alarm) {
		if (alarm.isDateInPast()) { 
			return;
		}
		
		PendingIntent pendingIntent = createPendingIntentFor(context, alarm);
		alarmManager(context).set(
				AlarmManager.RTC_WAKEUP, 
				alarm.getDate().getTimeInMillis(),
				pendingIntent);
	}
	

	private static PendingIntent createPendingIntentFor(Context context, Alarm alarm) {
		Intent intent = new Intent(context, AlarmReceiverActivity.class);
		intent.putExtra("Alarm", alarm);
		return PendingIntent.getActivity(
				context,
				(int) alarm.getId(), 
				intent, 
				PendingIntent.FLAG_CANCEL_CURRENT);
	}

	private static AlarmManager alarmManager(Context context) {
		return (AlarmManager) context
				.getSystemService(Activity.ALARM_SERVICE);
	}


}
