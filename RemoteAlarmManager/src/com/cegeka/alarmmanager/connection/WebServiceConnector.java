package com.cegeka.alarmmanager.connection;

import java.util.ArrayList;
import com.cegeka.alarmmanager.connection.model.Alarm;
import com.cegeka.alarmmanager.connection.model.User;
import com.cegeka.alarmmanager.exceptions.WebserviceException;

public class WebServiceConnector implements ConnectionInterface{
	
	/**
	 * @see ConnectionInterface
	 */
	@Override
	public User getUser(String email, String paswoord) throws WebserviceException{
		RequestTask r = (RequestTask) new RequestTask().execute(RequestTask.GET_USER,email, paswoord);
		//TODO Find a way to remove busy waiting.
		while(!r.isDone() && !r.isTimeout()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(r.isTimeout()){
			throw new WebserviceException("Connection timed out");
		}
		return r.getUser();
	}

	/**
	 * @see ConnectionInterface
	 */
	@Override
	public ArrayList<Alarm> getAlarmsFromUser(User u) throws WebserviceException{
		RequestTask r = (RequestTask) new RequestTask().execute(RequestTask.GET_ALARMS_FROM_USER, u.getEmailadres(), u.getPaswoord());
		//TODO Find a way to remove busy waiting.
		while(!r.isDone() && !r.isTimeout()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(r.isTimeout()){
			throw new WebserviceException("Connection timed out");
		}
		return r.getAlarms();
	}
	
	/**
	 * @see ConnectionInterface
	 */
	@Override
	public ArrayList<Alarm> getAlarmsFromUser(String email, String paswoord) throws WebserviceException{
		RequestTask r = (RequestTask) new RequestTask().execute(RequestTask.GET_ALARMS_FROM_USER,email, paswoord);
		//TODO Find a way to remove busy waiting.
		while(!r.isDone() && !r.isTimeout()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(r.isTimeout()){
			throw new WebserviceException("Connection timed out");
		}
		return r.getAlarms();
	}

}
