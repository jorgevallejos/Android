package com.cegeka.alarmmanager.exceptions;

public class WebserviceException extends Exception{
	public WebserviceException(){
		super();
	}
	
	public WebserviceException(String msg){
		super(msg);
	}
	
	public WebserviceException(Exception e){
		super(e);
	}
}
