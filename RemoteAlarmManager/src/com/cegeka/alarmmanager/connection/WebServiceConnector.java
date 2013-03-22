package com.cegeka.alarmmanager.connection;

import java.util.ArrayList;
import com.cegeka.alarmmanager.connection.model.Alarm;
import com.cegeka.alarmmanager.connection.model.User;
import com.cegeka.alarmmanager.exceptions.WebserviceException;

public class WebServiceConnector implements ConnectionInterface{
	
	@Override
	public User getUser(String email, String paswoord) throws WebserviceException{
		RequestTask r = (RequestTask) new RequestTask().execute(RequestTask.GET_USER,email, paswoord);
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

	@Override
	public ArrayList<Alarm> getAlarmsFromUser(User u) throws WebserviceException{
		RequestTask r = (RequestTask) new RequestTask().execute(RequestTask.GET_ALARMS_FROM_USER, u.getEmailadres(), u.getPaswoord());
		while(!r.isDone() && !r.isTimeout()){
			try {
				Thread.sleep(100);
				System.out.println("waiting for shizzle");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(r.isTimeout()){
			throw new WebserviceException("Connection timed out");
		}
		return r.getAlarms();
	}

	@Override
	public ArrayList<Alarm> getAlarmsFromUser(String email, String paswoord) throws WebserviceException{
		RequestTask r = (RequestTask) new RequestTask().execute(RequestTask.GET_ALARMS_FROM_USER,email, paswoord);
		while(!r.isDone() && !r.isTimeout()){
			try {
				Thread.sleep(100);
				System.out.println("waiting for shizzle");
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
