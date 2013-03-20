package com.cegeka.alarmtest.view;

import java.util.List;

import com.cegeka.alarmtest.db.Alarm;
import com.cegeka.alarmtest.db.AlarmsDataSource;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		AlarmsDataSource alarmDS = new AlarmsDataSource(getApplicationContext());
		alarmDS.open();
		List<Alarm> alarms = alarmDS.getAllAlarms();
		DebugWriter.writeToFile(alarms);
		for(Alarm a : alarms){
			AlarmScheduler.scheduleAlarm(a);
		}
		alarmDS.close();
		return START_STICKY;
	}

}
