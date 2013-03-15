package com.cegeka.alarmtest.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.cegeka.alarmtest.db.Alarm.Repeat_Unit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AlarmsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private AlarmSQLHelper dbHelper;
	private String[] allColumns = { 
			AlarmSQLHelper.COLUMN_ID,
			AlarmSQLHelper.COLUMN_TITLE, 
			AlarmSQLHelper.COLUMN_DESCR, 
			AlarmSQLHelper.COLUMN_DATE,
			AlarmSQLHelper.COLUMN_REPEATED,
			AlarmSQLHelper.COLUMN_REPEAT_UNIT,
			AlarmSQLHelper.COLUMN_REPEAT_UNIT_QUANTITY,
			AlarmSQLHelper.COLUMN_REPEAT_END_DATE
	};

	public AlarmsDataSource(Context context) {
		dbHelper = new AlarmSQLHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Alarm createAlarm(String title, String description, Calendar date, boolean repeated, 
			Repeat_Unit repeatUnit, int repeatUnitQuantity, Calendar repeatEndDate) 
	{

		ContentValues values = new ContentValues();

		values.put(AlarmSQLHelper.COLUMN_TITLE, title);
		values.put(AlarmSQLHelper.COLUMN_DESCR, description);
		values.put(AlarmSQLHelper.COLUMN_DATE, date.getTimeInMillis());
		values.put(AlarmSQLHelper.COLUMN_REPEATED, repeated);
		values.put(AlarmSQLHelper.COLUMN_REPEAT_UNIT, repeatUnit.ordinal());
		values.put(AlarmSQLHelper.COLUMN_REPEAT_UNIT_QUANTITY, repeatUnitQuantity);
		values.put(AlarmSQLHelper.COLUMN_REPEAT_END_DATE, repeatEndDate.getTimeInMillis());

		long insertId = database.insert(AlarmSQLHelper.TABLE_ALARMS, null, values);
		Cursor cursor = database.query(AlarmSQLHelper.TABLE_ALARMS, allColumns, AlarmSQLHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Alarm newAlarm = cursorToAlarm(cursor);
		cursor.close();
		return newAlarm;
		
	}
	
	public Alarm createAlarm(Alarm alarm) 
	{

		ContentValues values = new ContentValues();

		values.put(AlarmSQLHelper.COLUMN_TITLE, alarm.getTitle());
		values.put(AlarmSQLHelper.COLUMN_DESCR, alarm.getDescription());
		values.put(AlarmSQLHelper.COLUMN_DATE, alarm.getDate().getTimeInMillis());
		values.put(AlarmSQLHelper.COLUMN_REPEATED, alarm.isRepeated());
		values.put(AlarmSQLHelper.COLUMN_REPEAT_UNIT, alarm.getRepeatUnit().ordinal());
		values.put(AlarmSQLHelper.COLUMN_REPEAT_UNIT_QUANTITY, alarm.getRepeatUnitQuantity());
		values.put(AlarmSQLHelper.COLUMN_REPEAT_END_DATE, alarm.getRepeatEndDate().getTimeInMillis());

		long insertId = database.insert(AlarmSQLHelper.TABLE_ALARMS, null, values);
		Cursor cursor = database.query(AlarmSQLHelper.TABLE_ALARMS, allColumns, AlarmSQLHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Alarm newAlarm = cursorToAlarm(cursor);
		cursor.close();
		return newAlarm;
		
	}

	public void deleteAlarm(Alarm alarm) 
	{
		
		long id = alarm.getId();
		System.out.println("Comment deleted with id: " + id);
		database.delete(AlarmSQLHelper.TABLE_ALARMS, AlarmSQLHelper.COLUMN_ID + " = " + id, null);
		
	}

	public List<Alarm> getAllAlarms() 
	{
		
		List<Alarm> alarms = new ArrayList<Alarm>();

		Cursor cursor = database.query(AlarmSQLHelper.TABLE_ALARMS, allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			Alarm alarm = cursorToAlarm(cursor);
			alarms.add(alarm);
			cursor.moveToNext();
		}
		
		cursor.close();
		return alarms;
		
	}

	private Alarm cursorToAlarm(Cursor cursor) 
	{
		
		Alarm alarm = new Alarm();
		
		alarm.setId(cursor.getLong(0));
		alarm.setTitle(cursor.getString(1));
		alarm.setDescription(cursor.getString(2));
		alarm.setDate(getMillis(cursor.getString(3)));
		alarm.setRepeated(cursor.getInt(4) == 1);
		alarm.setRepeatUnit(Repeat_Unit.values()[cursor.getInt(5)]);
		alarm.setRepeatUnitQuantity(cursor.getInt(6));
		alarm.setRepeatEndDate(getMillis(cursor.getString(7)));
		
		return alarm;
		
	}
	
	private Calendar getMillis(String millisString)
	{
		
		Calendar cal = Calendar.getInstance();
		long millis = Long.parseLong(millisString);
		cal.setTimeInMillis(millis);
		return cal;
		
	}
}
