package com.example.alarmclienttest;

import java.util.ArrayList;

import be.cegeka.model.Alarm;
import be.cegeka.model.User;

public class WebServiceConnector implements ConnectionInterface{
	
	@Override
	public User getUser(String email) {
		new RequestTask().execute(RequestTask.GET_USER,"david.s.maes@gmail.com");
		return RequestTask.user;
	}

	@Override
	public ArrayList<Alarm> getAlarmsFromUser(User u) {
		new RequestTask().execute(RequestTask.GET_ALARMS_FROM_USER,"david.s.maes@gmail.com");
		return RequestTask.alarms;
	}

	@Override
	public ArrayList<Alarm> getAlarmsFromUser(String email) {
		new RequestTask().execute(RequestTask.GET_ALARMS_FROM_USER,email);
		return RequestTask.alarms;
	}

}
