package com.cegeka.alarmtest.view;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.cegeka.alarmtest.R;
import com.cegeka.alarmtest.R.id;
import com.cegeka.alarmtest.R.layout;
import com.cegeka.alarmtest.db.Alarm;
import com.cegeka.alarmtest.db.AlarmsDataSource;
import com.cegeka.alarmtest.db.Alarm.Repeat_Unit;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmReceiverActivity extends Activity {

	private MediaPlayer mMediaPlayer;
	private Alarm alarm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.alarm);
		
		setAlarm((Alarm) getIntent().getSerializableExtra("Alarm"));
		setNextAlarm();
		
		TextView titelfield = (TextView) findViewById(R.id.textView1);
		TextView descrfield = (TextView) findViewById(R.id.textView2);

		titelfield.setText(getAlarm().getTitle());
		descrfield.setText(getAlarm().getDescription());

		Button stopAlarm = (Button) findViewById(R.id.stopAlarm);
		stopAlarm.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mMediaPlayer.stop();
				finish();
				return false;
			}
		});

		playSound(this, getAlarmUri());
		deleteAlarm(getAlarm());
		AlarmsDataSource alarmDS = new AlarmsDataSource(this);
		alarmDS.open();
		List<Alarm> alarms = alarmDS.getAllAlarms();
		DebugWriter.writeToFile(alarms);
		alarmDS.close();
	}

	private void playSound(Context context, Uri alert) {
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.setVolume(0.02f, 0.02f);
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

	private void setNextAlarm(){
		
		if(getAlarm().isRepeated()){

			Repeat_Unit unit = getAlarm().getRepeatUnit();
			int repeatQuantity = getAlarm().getRepeatUnitQuantity();
			Calendar calRepeat = getAlarm().getRepeatEndDate();
			
			System.out.println(unit.toString());
			System.out.println(repeatQuantity);

			if(calRepeat.after(Calendar.getInstance())){
				Calendar newCal = Calendar.getInstance();
				
				System.out.println(newCal.get(Calendar.MINUTE));
				
				switch(unit){
				
					case MINUTE	:	newCal.add(Calendar.SECOND, repeatQuantity);
					case HOUR	:	newCal.add(Calendar.HOUR, repeatQuantity);
					case DAY	: 	newCal.add(Calendar.DATE, repeatQuantity);
					case WEEK	: 	newCal.add(Calendar.WEEK_OF_YEAR, repeatQuantity);
					case MONTH	: 	newCal.add(Calendar.MONTH, repeatQuantity);
					case YEAR	: 	newCal.add(Calendar.YEAR, repeatQuantity);
					
				}
				
				System.out.println(newCal.get(Calendar.MINUTE));
				
				Alarm newAlarm = getAlarm().clone();
				newAlarm.setDate(newCal);
				AlarmScheduler.scheduleAlarm(newAlarm);
			}
		}
	}

	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}
}
