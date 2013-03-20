package com.cegeka.alarmmanager.view;

import java.util.Calendar;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.cegeka.alarmmanager.db.Alarm;
import com.cegeka.alarmmanager.db.AlarmsDataSource;
import com.cegeka.alarmmanager.exceptions.AlarmException;
import com.cegeka.alarmmanager.exceptions.DatabaseException;
import com.cegeka.alarmtest.R;
import com.cegeka.debug.LogFileWriter;

public class MainActivity extends Activity {

	AlarmsDataSource alarmDS = new AlarmsDataSource(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		alarmDS.open();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void schedule(View view){
		try {

			// Date eerste keer alarm
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, 2);
			Alarm alarm = new Alarm("TestAlarm", cal.get(Calendar.MINUTE)+"", cal);

			alarm = alarmDS.createAlarm(alarm);
			AlarmScheduler.scheduleAlarm(this, alarm);

		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (AlarmException e) {
			e.printStackTrace();
		}
	}

	public void startService(View view){
		Intent intent = new Intent(this, AlarmService.class);
		startService(intent);
	}

	@Override
	protected void onDestroy() {
		alarmDS.close();
		super.onDestroy();
	}

	public void isMyServiceRunning(View view) {
		TextView tv = (TextView) findViewById(R.id.textView1);
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (AlarmService.class.getName().equals(service.service.getClassName())) {
				tv.setText("Service is running");
			}
		}
	}

	public void addAlarmsToDB(View view){
		try {
			LogFileWriter.writeLogLine("Add alarms to DB");
			AlarmsDataSource alarmDS = new AlarmsDataSource(this);
			alarmDS.open();
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, 130);

			Alarm alarm1 = new Alarm("Alarm 1", "Test alarm 1", cal);
			alarmDS.createAlarm(alarm1);

			cal.add(Calendar.SECOND, 10);
			Alarm alarm2 = new Alarm("Alarm 2", "Test alarm 2", cal);

			alarmDS.createAlarm(alarm2);

			cal.add(Calendar.SECOND, 10);
			Alarm alarm3 = new Alarm("Alarm 3", "Test alarm 3", cal);
			alarmDS.createAlarm(alarm3);
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (AlarmException e) {
			e.printStackTrace();
		}
	}



}
