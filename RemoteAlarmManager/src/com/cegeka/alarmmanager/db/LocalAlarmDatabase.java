package com.cegeka.alarmmanager.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cegeka.alarmmanager.exceptions.AlarmException;
import com.cegeka.alarmmanager.exceptions.DatabaseException;
import com.cegeka.alarmmanager.model.Alarm;
import com.cegeka.alarmmanager.model.Repeat_Unit;
import com.cegeka.alarmmanager.model.RepeatedAlarm;

class LocalAlarmDatabase {

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

	public LocalAlarmDatabase(Context context) {
		dbHelper = new AlarmSQLHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
//		database.beginTransaction();
	}

	public void close() {
		dbHelper.close();
//		database.endTransaction();
	}
	
	public void setTransactionSuccesfull()
	{
//		database.setTransactionSuccessful();
	}
	
	
	public void removeAll()
	{
		database.delete(AlarmSQLHelper.TABLE_ALARMS, "", null);
	}

	public RepeatedAlarm createRepeatedAlarm(RepeatedAlarm alarm) throws DatabaseException 
	{
		ContentValues values = getRepeatedAlarmContentValues(alarm);
		long insertId = database.insert(AlarmSQLHelper.TABLE_ALARMS, null, values);
		Cursor cursor = database.query(AlarmSQLHelper.TABLE_ALARMS, allColumns, AlarmSQLHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		RepeatedAlarm newAlarm = cursorToRepeatedAlarm(cursor);
		cursor.close();
		return newAlarm;

	}

	private ContentValues getRepeatedAlarmContentValues(RepeatedAlarm alarm) {
		ContentValues values = new ContentValues();
		values.put(AlarmSQLHelper.COLUMN_ID, alarm.getId());
		values.put(AlarmSQLHelper.COLUMN_TITLE, alarm.getTitle());
		values.put(AlarmSQLHelper.COLUMN_DESCR, alarm.getDescription());
		values.put(AlarmSQLHelper.COLUMN_DATE, alarm.getDate().getTimeInMillis());
		values.put(AlarmSQLHelper.COLUMN_REPEATED, true);
		values.put(AlarmSQLHelper.COLUMN_REPEAT_UNIT, alarm.getRepeatUnit().ordinal());
		values.put(AlarmSQLHelper.COLUMN_REPEAT_UNIT_QUANTITY, alarm.getRepeatUnitQuantity());
		values.put(AlarmSQLHelper.COLUMN_REPEAT_END_DATE, alarm.getRepeatEndDate().getTimeInMillis());
		return values;
	}

	public Alarm createAlarm(Alarm alarm) throws DatabaseException 
	{

		ContentValues values = getAlarmContentValues(alarm);
		long insertId = database.insert(AlarmSQLHelper.TABLE_ALARMS, null, values);
		Cursor cursor = database.query(AlarmSQLHelper.TABLE_ALARMS, allColumns, AlarmSQLHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Alarm newAlarm = cursorToAlarm(cursor);
		cursor.close();
		return newAlarm;

	}
	
	
	public void storeAlarms(List<Alarm> alarms) throws DatabaseException
	{
		for(Alarm alarm : alarms)
		{
			if(alarm instanceof RepeatedAlarm)
			{
				createRepeatedAlarm((RepeatedAlarm) alarm);
			}
			else
			{
				createAlarm(alarm);
			}
		}
	}

	private ContentValues getAlarmContentValues(Alarm alarm) {
		ContentValues values = new ContentValues();
		values.put(AlarmSQLHelper.COLUMN_ID, alarm.getId());
		values.put(AlarmSQLHelper.COLUMN_TITLE, alarm.getTitle());
		values.put(AlarmSQLHelper.COLUMN_DESCR, alarm.getDescription());
		values.put(AlarmSQLHelper.COLUMN_DATE, alarm.getDate().getTimeInMillis());
		values.put(AlarmSQLHelper.COLUMN_REPEATED, false);
		values.putNull(AlarmSQLHelper.COLUMN_REPEAT_UNIT);
		values.putNull(AlarmSQLHelper.COLUMN_REPEAT_UNIT_QUANTITY);
		values.putNull(AlarmSQLHelper.COLUMN_REPEAT_END_DATE);
		return values;
	}

	public void deleteAlarm(Alarm alarm) 
	{
		long id = alarm.getId();
		database.delete(AlarmSQLHelper.TABLE_ALARMS, AlarmSQLHelper.COLUMN_ID + " = " + id, null);
	}

	public Alarm updateAlarm(Alarm alarm)
	{
		ContentValues values = getAlarmContentValues(alarm);
		String where = "id=?";
		String[] whereArgs = new String[] {String.valueOf(alarm.getId())};
		database.update(AlarmSQLHelper.TABLE_ALARMS, values, where, whereArgs);
		return (Alarm) getAlarmById(alarm.getId());
	}

	public RepeatedAlarm updateRepeatedAlarm(RepeatedAlarm alarm)
	{
		ContentValues values = getRepeatedAlarmContentValues(alarm);
		String where = "id=?";
		String[] whereArgs = new String[] {String.valueOf(alarm.getId())};
		database.update(AlarmSQLHelper.TABLE_ALARMS, values, where, whereArgs);
		return (RepeatedAlarm) getAlarmById(alarm.getId());
	}

	public ArrayList<Alarm> getAllAlarms() throws DatabaseException 
	{
		
		ArrayList<Alarm> alarms = new ArrayList<Alarm>();
		Cursor cursor = database.query(AlarmSQLHelper.TABLE_ALARMS, allColumns, null, null, null, null, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Alarm alarm;
			if(cursor.getInt(4) == 1){
				alarm = cursorToRepeatedAlarm(cursor);
			}
			else {
				alarm = cursorToAlarm(cursor);
			}
			alarms.add(alarm);
			cursor.moveToNext();
		}

		cursor.close();
		return alarms;

	}

	private RepeatedAlarm cursorToRepeatedAlarm(Cursor cursor) throws DatabaseException 
	{
		try {

			RepeatedAlarm alarm = new RepeatedAlarm();

			alarm.setId(cursor.getLong(0));
			alarm.setTitle(cursor.getString(1));
			alarm.setDescription(cursor.getString(2));
			alarm.setDate(getMillis(cursor.getString(3)));
			alarm.setRepeatUnit(Repeat_Unit.values()[cursor.getInt(5)]);
			alarm.setRepeatUnitQuantity(cursor.getInt(6));
			alarm.setRepeatEndDate(getMillis(cursor.getString(7)));

			return alarm;

		} catch (AlarmException e) {
			throw new DatabaseException(e);
		}
	}

	private Alarm cursorToAlarm(Cursor cursor) throws DatabaseException{

		try {

			Alarm alarm = new Alarm();

			alarm.setId(cursor.getLong(0));
			alarm.setTitle(cursor.getString(1));
			alarm.setDescription(cursor.getString(2));
			alarm.setDate(getMillis(cursor.getString(3)));

			return alarm;

		}
		catch (AlarmException e) {
			throw new DatabaseException(e);
		}
	}

	private Calendar getMillis(String millisString)
	{

		Calendar cal = Calendar.getInstance();
		long millis = Long.parseLong(millisString);
		cal.setTimeInMillis(millis);
		return cal;

	}

	public void cleanup(){

		Cursor cursor = database.query(AlarmSQLHelper.TABLE_ALARMS, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		List<Long> idsToDelete = new ArrayList<Long>();

		while(!cursor.isAfterLast()){

			// If repeating alarm
			if(cursor.getInt(4) == 1){
				Calendar repeatEndDate = getMillis(cursor.getString(7));
				Calendar date = getMillis(cursor.getString(3));
				Repeat_Unit unit = Repeat_Unit.values()[cursor.getInt(5)];
				int repeatUnitQuantity = cursor.getInt(6);
				if(date.before(Calendar.getInstance())){

					while(date.before(Calendar.getInstance()) && date.before(repeatEndDate)){
						date.add(unit.getCalendarUnit(), repeatUnitQuantity);
					}

					if(date.after(Calendar.getInstance()) && date.before(repeatEndDate)){
						ContentValues values = new ContentValues();
						values.put(AlarmSQLHelper.COLUMN_DATE, date.getTimeInMillis());
						String[] whereArgs = new String[] {String.valueOf(cursor.getLong(0))};
						database.update(AlarmSQLHelper.TABLE_ALARMS, values, AlarmSQLHelper.COLUMN_ID+"=?", whereArgs);
					}
					else {
						idsToDelete.add(cursor.getLong(0));
					}
				}
			}
			else {
				Calendar date = getMillis(cursor.getString(3));
				if(date.before(Calendar.getInstance())){
					idsToDelete.add(cursor.getLong(0));
				}
			}
			cursor.moveToNext();
		}

		for(Long id : idsToDelete){
			database.delete(AlarmSQLHelper.TABLE_ALARMS, AlarmSQLHelper.COLUMN_ID + " = " + id, null);
		}
		cursor.close();
	}


	public Alarm getAlarmById(long id){
		Alarm alarm = null;
		Cursor cursor = database.query(AlarmSQLHelper.TABLE_ALARMS, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		boolean found =false;
		while(!cursor.isAfterLast() && !found){
			// If repeating alarm
			if(cursor.getInt(0) == id){
				alarm = createAlarmFromCursor(cursor); 
				found=true;
			}
			cursor.moveToNext();
		}
		cursor.close();
		return alarm;
	}

	public void deleteAlarmsByIds(ArrayList<Long> idsToDelete){
		for(Long id : idsToDelete){
			database.delete(AlarmSQLHelper.TABLE_ALARMS, AlarmSQLHelper.COLUMN_ID + " = " + id, null);
		}
	}

	public void deleteAlarms(ArrayList<Alarm> alarms){
		for(Alarm alarm : alarms){
			if(alarm!=null){
				database.delete(AlarmSQLHelper.TABLE_ALARMS, AlarmSQLHelper.COLUMN_ID + " = " + alarm.getId(), null);
			}
		}
	}

	public ArrayList<Alarm> getAllAlarmsExcept(ArrayList<Alarm> alarms){
		ArrayList<Alarm> alarmsresult = new ArrayList<Alarm>();
		Cursor cursor = database.query(AlarmSQLHelper.TABLE_ALARMS, allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			long id = cursor.getLong(0);
			boolean contains = false;
			for(Alarm a : alarms){
				if(id==a.getId()){
					contains=true;
				}
			}
			if(!contains){
				alarmsresult.add(createAlarmFromCursor(cursor));
			}
			cursor.moveToNext();
		}
		cursor.close();
		return alarmsresult;
	}

	private Alarm createAlarmFromCursor(Cursor cursor){
		Alarm alarm = null;
		if(cursor.getInt(4) == 1){
			Calendar date = getMillis(cursor.getString(3));
			Calendar enddate = getMillis(cursor.getString(7));
			Repeat_Unit unit = Repeat_Unit.values()[cursor.getInt(5)];
			int repeatUnitQuantity = cursor.getInt(6);
			try {
				alarm = new RepeatedAlarm(cursor.getLong(0), cursor.getString(1), cursor.getString(2), date, unit, repeatUnitQuantity, enddate);
			} catch (AlarmException e) {
				e.printStackTrace();
			}
		}else{
			Calendar date = getMillis(cursor.getString(3));
			try {
				alarm = new Alarm(cursor.getLong(0), cursor.getString(1), cursor.getString(2), date);
			} catch (AlarmException e) {
				e.printStackTrace();
			}
		}
		return alarm;
	}

}
