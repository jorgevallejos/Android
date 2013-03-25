package com.cegeka.alarmmanager.connection;

import java.util.ArrayList;

import com.cegeka.alarmmanager.connection.model.Alarm;
import com.cegeka.alarmmanager.connection.model.User;
import com.cegeka.alarmmanager.exceptions.WebserviceException;


public interface ConnectionInterface {
	public User getUser(String email, String password) throws WebserviceException;
	public ArrayList<Alarm> getAlarmsFromUser(User u) throws WebserviceException;
	public ArrayList<Alarm> getAlarmsFromUser(String email, String paswoord) throws WebserviceException;
}
