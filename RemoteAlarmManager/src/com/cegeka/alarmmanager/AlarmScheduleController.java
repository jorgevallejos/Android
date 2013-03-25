package com.cegeka.alarmmanager;

import java.util.ArrayList;
import java.util.Calendar;
import android.content.Context;
import com.cegeka.alarmmanager.db.Alarm;
import com.cegeka.alarmmanager.db.AlarmsDataSource;
import com.cegeka.alarmmanager.db.RepeatedAlarm;
import com.cegeka.alarmmanager.db.RepeatedAlarm.Repeat_Unit;
import com.cegeka.alarmmanager.exceptions.AlarmException;
import com.cegeka.alarmmanager.exceptions.DatabaseException;
import com.cegeka.alarmmanager.view.AlarmScheduler;

public class AlarmScheduleController {

	public static void scheduleAlarm(Alarm alarm, Context ctx){
		AlarmsDataSource alarmDS = new AlarmsDataSource(ctx);
		alarmDS.open();
		try {
			if(alarmDS.getAlarmById(alarm.getId()) == null){
				alarm = alarmDS.createAlarm(alarm);
				AlarmScheduler.scheduleAlarm(ctx, alarm);
			}
		} catch (DatabaseException e) {
			e.printStackTrace();
		}finally{
			alarmDS.close();
		}
		alarmDS.close();
	}

	public static void scheduleAlarms(ArrayList<Alarm> alarms, Context ctx){
		AlarmsDataSource alarmDS = new AlarmsDataSource(ctx);
		alarmDS.open();
		try {
			
			for(Alarm a : alarms){
				if(alarmDS.getAlarmById(a.getId()) == null){
					if(a instanceof RepeatedAlarm){
						RepeatedAlarm ra = (RepeatedAlarm) a;
						a = alarmDS.createRepeatedAlarm(ra);
					}else{
						a = alarmDS.createAlarm(a);
					}
					AlarmScheduler.scheduleAlarm(ctx, a);
				}
			}
			ArrayList<Alarm> removeAlarmsList = alarmDS.getAllAlarmsExcept(alarms);
			alarmDS.deleteAlarms(removeAlarmsList);
			AlarmScheduler.cancelAlarms(ctx, removeAlarmsList);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}finally{
			alarmDS.cleanup();
			alarmDS.close();
		}
		alarmDS.close();
	}


	public static void cancelAlarm(Alarm alarm, Context ctx){

	}

	public static Alarm convertAlarm(com.cegeka.alarmmanager.connection.model.Alarm a) {
		Alarm alarm = null;

		if(a.isRepeated()){
			Calendar calendarDate = Calendar.getInstance();
			calendarDate.setTimeInMillis(a.getDate());
			Calendar calendarEndDate = Calendar.getInstance();
			calendarEndDate.setTimeInMillis(a.getRepeatEndDate());
			try{
				alarm = resetDateRepeatedAlarm(a);
			}catch(AlarmException e){
				e.printStackTrace();
			}
		}else{
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(a.getDate());
			try{
				alarm = new Alarm(a.getId(), a.getTitle(), a.getInfo(), c);
			}catch(AlarmException e){}
		}

		return alarm;
	}

	private static Alarm resetDateRepeatedAlarm(com.cegeka.alarmmanager.connection.model.Alarm alarm) throws AlarmException{
		Calendar repeatEndDate = getMillis(alarm.getRepeatEndDate());
		Calendar date = getMillis(alarm.getDate());
		Repeat_Unit unit = Repeat_Unit.valueOf(alarm.getRepeatUnit());
		int repeatUnitQuantity = alarm.getRepeatQuantity();
		RepeatedAlarm repeatedAlarm = null;
		if(date.before(Calendar.getInstance())){

			while(date.before(Calendar.getInstance()) && date.before(repeatEndDate)){

				switch(unit){

				case MINUTE	:	date.add(Calendar.MINUTE, repeatUnitQuantity); break;
				case HOUR	:	date.add(Calendar.HOUR, repeatUnitQuantity); break;
				case DAY	: 	date.add(Calendar.DATE, repeatUnitQuantity); break;
				case WEEK	: 	date.add(Calendar.WEEK_OF_YEAR, repeatUnitQuantity); break;
				case MONTH	: 	date.add(Calendar.MONTH, repeatUnitQuantity); break;
				case YEAR	: 	date.add(Calendar.YEAR, repeatUnitQuantity); break;

				}
			}

			if(date.after(Calendar.getInstance()) && date.before(repeatEndDate)){
				repeatedAlarm = new RepeatedAlarm(alarm.getId(), alarm.getTitle(), alarm.getInfo(), date, Repeat_Unit.valueOf(alarm.getRepeatUnit()), alarm.getRepeatQuantity(), repeatEndDate);
			}
			
		}
		return repeatedAlarm;
	}
	
	private static Calendar getMillis(long millis)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return cal;
	}
}
