package com.cegeka.alarmtest.view;

import java.util.Calendar;

import com.cegeka.alarmtest.R;
import com.cegeka.alarmtest.db.Alarm;
import com.cegeka.alarmtest.db.AlarmsDataSource;
import com.cegeka.alarmtest.db.Alarm.Repeat_Unit;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	AlarmsDataSource alarmDS = new AlarmsDataSource(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AlarmScheduler.context = this;
		alarmDS.open();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void schedule(View view){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 10);
		Calendar calRepeat = Calendar.getInstance();
		calRepeat.add(Calendar.MINUTE, 10);
		Alarm alarm = alarmDS.createAlarm("TestAlarm", cal.get(Calendar.MINUTE)+"", cal, true, Repeat_Unit.MINUTE, 10, calRepeat);
		AlarmScheduler.scheduleAlarm(alarm);
	}

	@Override
	protected void onDestroy() {
		alarmDS.close();
		super.onDestroy();
	}

	

}
