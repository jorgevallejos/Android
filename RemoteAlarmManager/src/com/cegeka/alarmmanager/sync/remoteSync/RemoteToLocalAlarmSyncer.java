package com.cegeka.alarmmanager.sync.remoteSync;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;

import com.cegeka.alarmmanager.db.LocalAlarmRepository;
import com.cegeka.alarmmanager.model.Alarm;
import com.cegeka.alarmmanager.model.User;
import com.cegeka.alarmmanager.sync.remoteSync.remoteDBConnection.RemoteDBWebConnection;
import com.cegeka.alarmmanager.utilities.UserLoginLogOut;

public class RemoteToLocalAlarmSyncer extends Observable{

	public void sync(final Context context)
	{
		User user = UserLoginLogOut.getLoggedInUser(context);
		
		RemoteDBWebConnection webServiceConnector = new RemoteDBWebConnection();
		webServiceConnector.addObserver(new Observer() {
			@Override
			public void update(Observable observable, Object data) {
				List<Alarm> alarms = ((RemoteDBWebConnection)observable).getAlarms();
				LocalAlarmRepository.replaceAll(context, alarms);
				setChanged();
				notifyObservers();
			}
		});
		webServiceConnector.startAlarmsFromUser(user);
	}
}
