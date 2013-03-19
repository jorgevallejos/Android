package com.cegeka.alarmmanager.db;

import java.util.Calendar;

import com.cegeka.alarmmanager.exceptions.AlarmException;

public class RepeatedAlarm extends Alarm {

	private static final long serialVersionUID = 1963194492622085969L;

	private Repeat_Unit repeatUnit;
	private int repeatUnitQuantity;
	private Calendar repeatEndDate;

	public RepeatedAlarm(){

	}

	public RepeatedAlarm(String title, String description, Calendar date, Repeat_Unit repeatUnit, int repeatQuantity, Calendar repeatEndDate) throws AlarmException{
		super(title, description, date);
		setRepeatUnit(repeatUnit);
		setRepeatUnitQuantity(repeatQuantity);
		setRepeatEndDate(repeatEndDate);
	}

	public RepeatedAlarm(RepeatedAlarm repeatedAlarm) throws AlarmException{
		setTitle(repeatedAlarm.getTitle());
		setDescription(repeatedAlarm.getDescription());
		setDate((Calendar) repeatedAlarm.getDate().clone());
		setRepeatUnit(repeatedAlarm.getRepeatUnit());
		setRepeatUnitQuantity(repeatedAlarm.getRepeatUnitQuantity());
		setRepeatEndDate((Calendar) repeatedAlarm.getRepeatEndDate().clone());
	}

	public Repeat_Unit getRepeatUnit() {
		
		return repeatUnit;
	}

	public void setRepeatUnit(Repeat_Unit repeatUnit) {
		
		this.repeatUnit = repeatUnit;
	}

	public int getRepeatUnitQuantity() {
		
		return repeatUnitQuantity;
	}

	public void setRepeatUnitQuantity(int repeatUnitQuantity) throws AlarmException {
		
		if(repeatUnitQuantity <= 0){
			throw new AlarmException("The repeatUnitQuantity can't be 0 or negative.");
		}
		
		this.repeatUnitQuantity = repeatUnitQuantity;
	}

	public Calendar getRepeatEndDate() {
		
		return repeatEndDate;
	}

	public void setRepeatEndDate(Calendar repeatEndDate) throws AlarmException {

		if(repeatEndDate == null){
			throw new AlarmException("The repeatEndDate can't be null.");
		}

		if(repeatEndDate.before(Calendar.getInstance())){
			throw new AlarmException("The repeat end date can't be in the past.");
		}

		if(getDate() != null){
			if(repeatEndDate.before(getDate())){
				throw new AlarmException("The repeat end date can't be before the event date.");
			}
		}

		this.repeatEndDate = repeatEndDate;
	}

	// Enum die aangeeft met welk repeat unit er wordt gewerkt.
	public enum Repeat_Unit {

		MINUTE,
		HOUR,
		DAY,
		WEEK,
		MONTH,
		YEAR;

		Repeat_Unit(){

		}
	}

}
