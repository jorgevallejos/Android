package com.example.alarmclienttest;

import java.util.ArrayList;

import be.cegeka.model.Alarm;
import be.cegeka.model.User;


public interface ConnectionInterface {
	public User getUser(String email);
	public ArrayList<Alarm> getAlarmsFromUser(User u);
	public ArrayList<Alarm> getAlarmsFromUser(String email);
}
