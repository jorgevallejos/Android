package com.cegeka.alarmmanager.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;

import com.cegeka.alarmmanager.exceptions.DatabaseException;
import com.cegeka.alarmmanager.exceptions.TechnicalException;
import com.cegeka.alarmmanager.model.Alarm;
import com.cegeka.alarmmanager.model.Repeat_Unit;
import com.cegeka.alarmmanager.model.RepeatedAlarm;

public class LocalAlarmRepository
{

	public static List<Alarm> getLocalAlarms(Context context)
	{
		LocalAlarmDatabase alarmsDataSource = new LocalAlarmDatabase(context);
		alarmsDataSource.open();
		List<Alarm> alarms = new ArrayList<Alarm>();
		try
		{
			alarms = alarmsDataSource.getAllAlarms();
		} catch (DatabaseException e)
		{
			throw new TechnicalException(e);
		} finally
		{
			alarmsDataSource.close();
		}
		return alarms;
	}

	public static void replaceAll(Context context, List<Alarm> alarms)
	{
		LocalAlarmDatabase alarmsDataSource = new LocalAlarmDatabase(context);
		alarmsDataSource.open();
		try
		{
			removeAllFromLocal(alarmsDataSource);
			insertAllNew(alarmsDataSource, alarms);
			alarmsDataSource.setTransactionSuccesfull();
		} catch (DatabaseException exception)
		{
			throw new TechnicalException(exception);
		} finally
		{
			alarmsDataSource.close();
		}
	}

	private static void removeAllFromLocal(LocalAlarmDatabase alarmsDataSource)
	{
		alarmsDataSource.removeAll();
	}

	private static void insertAllNew(LocalAlarmDatabase alarmsDataSource,
			List<Alarm> alarms) throws DatabaseException
	{
		alarmsDataSource.storeAlarms(alarms);
	}

	public static void deleteAlarm(Context context, Alarm alarm)
	{
		LocalAlarmDatabase alarmDS = new LocalAlarmDatabase(context);
		alarmDS.open();
		alarmDS.deleteAlarm(alarm);
		alarmDS.close();
	}

	public static RepeatedAlarm updateRepeatedAlarm(Context context,
			RepeatedAlarm repAlarm)
	{
		try
		{
			Repeat_Unit unit = repAlarm.getRepeatUnit();
			int repeatQuantity = repAlarm.getRepeatUnitQuantity();
			Calendar calRepeat = repAlarm.getRepeatEndDate();

			if (calRepeat.after(Calendar.getInstance()))
			{
				Calendar newCal = (Calendar) repAlarm.getDate().clone();
				newCal.add(unit.getCalendarUnit(), repeatQuantity);
				if (newCal.before(repAlarm.getRepeatEndDate()))
				{
					repAlarm.setDate(newCal);
					LocalAlarmDatabase alarmDS = new LocalAlarmDatabase(context);
					alarmDS.open();
					RepeatedAlarm newAlarm = alarmDS
							.updateRepeatedAlarm(repAlarm);
					alarmDS.close();

					return newAlarm;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
