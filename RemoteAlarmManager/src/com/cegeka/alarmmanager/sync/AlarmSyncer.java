package com.cegeka.alarmmanager.sync;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;

import com.cegeka.alarmmanager.sync.localSync.LocalToAndroidSchedulerAlarmSyncer;
import com.cegeka.alarmmanager.sync.remoteSync.RemoteToLocalAlarmSyncer;


public class AlarmSyncer extends Observable implements Observer{
	
	private static final AlarmSyncer instance = new AlarmSyncer();
	private AlarmSyncer(){}
	
	public static AlarmSyncer getInstance(){
		return instance;
	}
	
	public void syncAllAlarms(Context context)
	{
		LocalToAndroidSchedulerAlarmSyncer localToAndroidSchedulerAlarmSyncer = new LocalToAndroidSchedulerAlarmSyncer();
		RemoteToLocalAlarmSyncer remoteToLocalAlarmSyncer = new RemoteToLocalAlarmSyncer();
		
		remoteToLocalAlarmSyncer.addObserver(this);
		
		localToAndroidSchedulerAlarmSyncer.unscheduleAllAlarms(context);
		remoteToLocalAlarmSyncer.sync(context);
		localToAndroidSchedulerAlarmSyncer.scheduleAllAlarms(context);
	}

	@Override
	public void update(Observable observable, Object data) {
		setChanged();
		notifyObservers();
	}
}
