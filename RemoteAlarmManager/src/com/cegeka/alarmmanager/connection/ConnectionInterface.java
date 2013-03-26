package com.cegeka.alarmmanager.connection;

import java.util.ArrayList;

import com.cegeka.alarmmanager.connection.model.Alarm;
import com.cegeka.alarmmanager.connection.model.User;
import com.cegeka.alarmmanager.exceptions.AlarmException;
import com.cegeka.alarmmanager.exceptions.WebserviceException;


public interface ConnectionInterface {
	/**
	 * Get a User using its login credentials if they are wrong this method will return null. 
	 * @param email The email address of the user. 
	 * @param password The password of the user.
	 * @return An {@link User} object containing all the information off the user. Or null if the login failed.
	 * @throws WebserviceException Thrown when connection timed out
	 */
	public User getUser(String email, String password) throws WebserviceException;
	
	/**
	 * Get all the alarms of a user.
	 * @param u The {@link User}
	 * @return An {@link ArrayList} of {@link AlarmException} objects.
	 * @throws WebserviceException Thrown when connection timed out
	 */
	public ArrayList<Alarm> getAlarmsFromUser(User u) throws WebserviceException;
	
	/**
	 * Get all the alarms of a user using its login credentials.
	 * @param email The email address of the user. 
	 * @param password The password of the user.
	 * @return An {@link ArrayList} of {@link AlarmException} objects.
	 * @throws WebserviceException Thrown when connection timed out
	 */
	public ArrayList<Alarm> getAlarmsFromUser(String email, String password) throws WebserviceException;
}
