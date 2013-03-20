package com.cegeka.alarmmanager.exceptions;

public class AlarmException extends Exception{
	
	public AlarmException(){
		super();
	}
	
	public AlarmException(String msg){
		super(msg);
	}
	
	public AlarmException(Exception e){
		super(e);
	}

}
