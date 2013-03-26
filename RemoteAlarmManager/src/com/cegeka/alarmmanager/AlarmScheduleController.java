package com.cegeka.alarmmanager;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.content.Context;
import android.content.res.Resources.Theme;

import com.cegeka.alarmmanager.db.Alarm;
import com.cegeka.alarmmanager.db.AlarmsDataSource;
import com.cegeka.alarmmanager.db.RepeatedAlarm;
import com.cegeka.alarmmanager.db.RepeatedAlarm.Repeat_Unit;
import com.cegeka.alarmmanager.exceptions.AlarmException;
import com.cegeka.alarmmanager.exceptions.DatabaseException;
import com.cegeka.alarmmanager.view.AlarmScheduler;

public class AlarmScheduleController {

	

	/**
	 * Tries to add all the {@link Alarm} objects to the database and schedule them.
	 * If the {@link Alarm} given does not already exists in the database it adds the {@link Alarm} to the SQLite database and 
	 * also schedules the {@link Alarm} using the {@link AlarmScheduler} else it does nothing with the particular {@link Alarm}.
	 * It also removes Alarms from the scheduler and removes Alarms from the database if the {@link ArrayList} of {@link Alarm} objects given does not contain them anymore.
	 * @param alarm The {@link Alarm} to schedule
	 * @param ctx A context needed for the {@link AlarmScheduler}.
	 */
	public static void updateAlarms(ArrayList<Alarm> alarms, Context ctx){
		AlarmsDataSource alarmDS = new AlarmsDataSource(ctx);
		alarmDS.open();
		try {
			
			for(Alarm a : alarms){
				Alarm alarm = alarmDS.getAlarmById(a.getId());
				if(alarm == null){
					if(a instanceof RepeatedAlarm){
						RepeatedAlarm ra = (RepeatedAlarm) a;
						a = alarmDS.createRepeatedAlarm(ra);
					}else{
						a = alarmDS.createAlarm(a);
					}
					
				}else{
					if(a instanceof RepeatedAlarm){
						RepeatedAlarm ra = (RepeatedAlarm) a;
						a = alarmDS.updateRepeatedAlarm(ra);
					}else{
						a = alarmDS.updateAlarm(a);
					}
				}
				AlarmScheduler.scheduleAlarm(ctx, a);
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

	/**
	 * Cancels the given Alarms and removes them from the database
	 * @param alarmen The {@link Alarm} objects to cancel.
	 * @param ctx The context
	 */
	public static void cancelAlarms(ArrayList<Alarm> alarmen, Context ctx){
		AlarmScheduler.cancelAlarms(ctx, alarmen);
		AlarmsDataSource alarmDS = new AlarmsDataSource(ctx);
		alarmDS.open();
		try{
			alarmDS.deleteAlarms(alarmen);
		}catch(Exception e){
			
		}finally{
			alarmDS.close();
		}
	}

	/**
	 * Converts an {@link com.cegeka.alarmmanager.connection.model.Alarm} object to an {@link Alarm} object.
	 * 
	 * @param a The {@link com.cegeka.alarmmanager.connection.model.Alarm} object to convert
	 * @return An {@link Alarm} object.
	 */
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

	/**
	 * Converts an {@link com.cegeka.alarmmanager.connection.model.Alarm} to an {@link RepeatedAlarm} while setting its end_date correctly.
	 * @param alarm The {@link com.cegeka.alarmmanager.connection.model.Alarm} object to convert.
	 * @return A {@link RepeatedAlarm}
	 * @throws AlarmException Thrown when the date of the alarm falls in the past.
	 */
	private static RepeatedAlarm resetDateRepeatedAlarm(com.cegeka.alarmmanager.connection.model.Alarm alarm) throws AlarmException{
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
	
	/**
	 * Get a {@link Calendar} from milliseconds.
	 * @param millis
	 * @return A {@link Calendar} object
	 */
	private static Calendar getMillis(long millis)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return cal;
	}
}
