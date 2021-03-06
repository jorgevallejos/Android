package com.cegeka.alarmmanager.sync.remoteSync.remoteDBConnection;

import java.util.ArrayList;
import java.util.Observer;

import com.cegeka.alarmmanager.exceptions.WebserviceException;
import com.cegeka.alarmmanager.model.Alarm;
import com.cegeka.alarmmanager.model.User;

public interface RemoteDBConnectionInterface {

	
	public abstract void startUserLogin(String email, String paswoord);

	public abstract void addObserver(Observer observer);

	public abstract User getUser();
	
	public abstract void startAlarmsFromUser(User u);
	
	public abstract void getAlarmsFromUser(String email, String paswoord)
			throws WebserviceException;

	public abstract ArrayList<Alarm> getAlarms();

}