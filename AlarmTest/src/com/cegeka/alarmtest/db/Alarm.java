package com.cegeka.alarmtest.db;

import java.io.Serializable;
import java.util.Calendar;

public class Alarm implements Serializable{
	
	private long id;
	private Calendar date;
	private String title;
	private String description;
	private boolean repeated;
	private Repeat_Unit repeatUnit;
	private int repeatUnitQuantity;
	private Calendar repeatEndDate;
	
	public Alarm(Calendar date, String title, String description) {
		setDate(date);
		setTitle(title);
		setDescription(description);
	}
	
	public Alarm(Alarm alarm) {
		setDate((Calendar)alarm.getDate().clone());
		setTitle(alarm.getTitle());
		setDescription(alarm.getDescription());
		setRepeated(alarm.isRepeated());
		setRepeatUnit(alarm.getRepeatUnit());
		setRepeatUnitQuantity(alarm.getRepeatUnitQuantity());
		setRepeatEndDate((Calendar)alarm.getRepeatEndDate().clone());
	}
	
	public Alarm(){
		
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String toString(){
		return "ID: " + getId() + "\nTitel: " +getTitle() + "\nDescription: " + getDescription() + "\nTimeInMillis: " + getDate().getTimeInMillis();
	}
	
	public boolean isRepeated() {
		return repeated;
	}

	public void setRepeated(boolean repeated) {
		this.repeated = repeated;
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

	public void setRepeatUnitQuantity(int repeatUnitQuantity) {
		this.repeatUnitQuantity = repeatUnitQuantity;
	}

	public Calendar getRepeatEndDate() {
		return repeatEndDate;
	}

	public void setRepeatEndDate(Calendar repeatEndDate) {
		this.repeatEndDate = repeatEndDate;
	}
	
	public Alarm clone(){
		return new Alarm(this);
	}

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
