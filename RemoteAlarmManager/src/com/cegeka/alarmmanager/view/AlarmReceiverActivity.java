package com.cegeka.alarmmanager.view;

import java.io.IOException;
import java.util.Calendar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import com.cegeka.alarmmanager.db.Alarm;
import com.cegeka.alarmmanager.db.AlarmsDataSource;
import com.cegeka.alarmmanager.db.RepeatedAlarm;
import com.cegeka.alarmmanager.db.RepeatedAlarm.Repeat_Unit;
import com.cegeka.alarmtest.R;

public class AlarmReceiverActivity extends Activity {

	private MediaPlayer mMediaPlayer;
	private Alarm alarm;
	boolean loaded = false;
	Dialog mDialog; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDialog = new Dialog(this);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		//setAlarm((Alarm) getIntent().getSerializableExtra("Alarm"));
		Object o = getIntent().getSerializableExtra("Alarm");
		if(o instanceof RepeatedAlarm) {
			setAlarm((RepeatedAlarm) o);
			setNextAlarm();
		}else{
			setAlarm((Alarm) o);
		}

		mDialog.setTitle(getAlarm().getTitle());
		mDialog.setCancelable(false);
		mDialog.setContentView(R.layout.alarm);

		TextView descrfield = (TextView) mDialog.findViewById(R.id.description);
		descrfield.setText(getAlarm().getDescription());

		Button stopAlarm = (Button) mDialog.findViewById(R.id.stopAlarm);
		stopAlarm.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mMediaPlayer.stop();
				finish();
				mDialog.cancel();
				return false;
			}
		});
		mDialog.show();
		playSound(this, getAlarmUri());
		if(!(getAlarm() instanceof RepeatedAlarm)){
			deleteAlarm(getAlarm());
		}
	}


	private void playSound(Context context, Uri alert) {
		mMediaPlayer = new MediaPlayer();
		((Activity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC); 
		try {
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				//mMediaPlayer.setVolume(0.01f, 0.01f);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (IOException e) {
			System.out.println("OOPS");
		}

	}



	private Uri getAlarmUri() {
		Uri alert = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if (alert == null) {
			alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if (alert == null) {
				alert = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		return alert;
	}

	private void deleteAlarm(Alarm alarm){
		AlarmsDataSource alarmDS = new AlarmsDataSource(getApplicationContext());
		alarmDS.open();
		alarmDS.deleteAlarm(alarm);
		alarmDS.close();
	}

	private void setNextAlarm() {

		try {

			RepeatedAlarm repAlarm = (RepeatedAlarm) getAlarm();

			Repeat_Unit unit = repAlarm.getRepeatUnit();
			int repeatQuantity = repAlarm.getRepeatUnitQuantity();
			Calendar calRepeat = repAlarm.getRepeatEndDate();

			if(calRepeat.after(Calendar.getInstance())){
				Calendar newCal = (Calendar) repAlarm.getDate().clone();

				switch(unit){

				case MINUTE	:	newCal.add(Calendar.MINUTE, repeatQuantity); break;
				case HOUR	:	newCal.add(Calendar.HOUR, repeatQuantity); break;
				case DAY	: 	newCal.add(Calendar.DATE, repeatQuantity); break;
				case WEEK	: 	newCal.add(Calendar.WEEK_OF_YEAR, repeatQuantity); break;
				case MONTH	: 	newCal.add(Calendar.MONTH, repeatQuantity); break;
				case YEAR	: 	newCal.add(Calendar.YEAR, repeatQuantity); break;

				}

				if(newCal.before(repAlarm.getRepeatEndDate())){
					repAlarm.setDate(newCal);
					//					RepeatedAlarm newAlarm = new RepeatedAlarm(repAlarm);
					AlarmsDataSource alarmDS = new AlarmsDataSource(this);
					alarmDS.open();
					RepeatedAlarm newAlarm = alarmDS.updateRepeatedAlarm(repAlarm);
					alarmDS.close();

					AlarmScheduler.scheduleAlarm(this, newAlarm);
				}
			}
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}

	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}
}
