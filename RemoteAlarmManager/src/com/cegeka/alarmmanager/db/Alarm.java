package com.cegeka.alarmmanager.db;

import java.io.Serializable;
import java.util.Calendar;

import com.cegeka.alarmmanager.exceptions.AlarmException;

public class Alarm implements Serializable{

	private static final long serialVersionUID = 7001802310281173015L;

	// Instantie variabelen
	private long id;
	private Calendar date;
	private String title;
	private String description;

	public Alarm(){

	}

	public Alarm(String title, String description, Calendar date) throws AlarmException {
		setDate(date);
		setTitle(title);
		setDescription(description);
	}

	public Alarm(Alarm alarm) throws AlarmException{
		setDate((Calendar) alarm.getDate().clone());
		setTitle(alarm.getTitle());
		setDescription(alarm.getDescription());
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) throws AlarmException {
		if(date == null){
			throw new AlarmException("The date can't be null.");
		}
		if(date.before(Calendar.getInstance())){
			throw new AlarmException("The date of the alarm can't be in the past.");
		}
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) throws AlarmException {
		if(title == null){
			throw new AlarmException("The title can't be null.");
		}
		if(title.trim().length() == 0){
			throw new AlarmException("The title can't be empty or white spaces.");
		}
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) throws AlarmException {
		if(description == null){
			throw new AlarmException("Description can't be null.");
		}
		if(description.trim().length() == 0){
			throw new AlarmException("Description can't be empty or white spaces.");
		}
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



}
