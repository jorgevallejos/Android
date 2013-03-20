package com.cegeka.alarmmanager.view;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.cegeka.alarmmanager.db.Alarm;
import com.cegeka.alarmmanager.db.AlarmsDataSource;
import com.cegeka.alarmmanager.exceptions.DatabaseException;
import com.cegeka.debug.LogFileWriter;

public class AlarmService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogFileWriter.writeLogLine("Service created");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			LogFileWriter.writeLogLine("Fix repeated alarms starts");
			fixRepeatedAlarms();
			LogFileWriter.writeLogLine("CleanupAndSetAlarms starts");
			cleanUpAndSetAlarms();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return START_STICKY;
	}

	private void cleanUpAndSetAlarms() throws DatabaseException{
		AlarmsDataSource alarmDS = new AlarmsDataSource(this);
		alarmDS.open();
		List<Alarm> alarms = alarmDS.getAllAlarms();
		for(Alarm a : alarms){
			AlarmScheduler.scheduleAlarm(this, a);
		}
		alarmDS.close();
	}

	private void fixRepeatedAlarms() throws DatabaseException{
		AlarmsDataSource alarmDS = new AlarmsDataSource(this);
		alarmDS.open();
		alarmDS.cleanup();
		alarmDS.close();
	}

}
