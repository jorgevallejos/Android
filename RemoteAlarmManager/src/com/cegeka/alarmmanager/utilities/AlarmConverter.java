package com.cegeka.alarmmanager.utilities;

import java.util.Calendar;

import com.cegeka.alarmmanager.exceptions.AlarmException;
import com.cegeka.alarmmanager.model.Alarm;
import com.cegeka.alarmmanager.model.Repeat_Unit;
import com.cegeka.alarmmanager.model.RepeatedAlarm;
import com.cegeka.alarmmanager.sync.remoteSync.connection.transferobject.AlarmTO;

public class AlarmConverter {
	/**
	 * Converts an {@link com.cegeka.alarmmanager.connection.transferobject.AlarmTO} object to an {@link Alarm} object.
	 * 
	 * @param a The {@link com.cegeka.alarmmanager.connection.transferobject.AlarmTO} object to convert
	 * @return An {@link Alarm} object.
	 */
	public static Alarm convertAlarm(AlarmTO a) {
		Alarm alarm = null;

		if(a.isRepeated()){

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
	 * Converts an {@link com.cegeka.alarmmanager.connection.transferobject.AlarmTO} to an {@link RepeatedAlarm} while setting its end_date correctly.
	 * @param alarm The {@link com.cegeka.alarmmanager.connection.transferobject.AlarmTO} object to convert.
	 * @return A {@link RepeatedAlarm}
	 * @throws AlarmException Thrown when the date of the alarm falls in the past.
	 */
	private static RepeatedAlarm resetDateRepeatedAlarm(AlarmTO alarm) throws AlarmException{
		Calendar repeatEndDate = getMillis(alarm.getRepeatEndDate());
		Calendar date = getMillis(alarm.getDate());
		Repeat_Unit unit = Repeat_Unit.valueOf(alarm.getRepeatUnit());
		int repeatUnitQuantity = alarm.getRepeatQuantity();
		RepeatedAlarm repeatedAlarm = null;
		if(date.before(Calendar.getInstance())){

			while(date.before(Calendar.getInstance()) && date.before(repeatEndDate)){
				date.add(unit.getCalendarUnit(), repeatUnitQuantity);
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
