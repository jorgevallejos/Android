package com.cegeka.alarmmanager.model;

import java.util.Calendar;

public enum Repeat_Unit {
	// Enum die aangeeft met welk repeat unit er wordt gewerkt.

	MINUTE(Calendar.MINUTE),
	HOUR(Calendar.HOUR),
	DAY(Calendar.DATE),
	WEEK(Calendar.WEEK_OF_YEAR),
	MONTH(Calendar.MONTH),
	YEAR(Calendar.YEAR);

	private int unit;

	Repeat_Unit(int unit){
		setUnit(unit);
	}
	public int getCalendarUnit() {
		return unit;
	}
	public void setUnit(int unit) {
		this.unit = unit;
	}

}
