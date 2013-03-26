package com.cegeka.alarmmanager.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import com.cegeka.alarmmanager.AlarmScheduleController;
import com.cegeka.alarmmanager.connection.UserLoaderSaver;
import com.cegeka.alarmmanager.connection.WebServiceConnector;
import com.cegeka.alarmmanager.connection.model.User;
import com.cegeka.alarmmanager.db.Alarm;
import com.cegeka.alarmmanager.db.AlarmsDataSource;
import com.cegeka.alarmmanager.exceptions.DatabaseException;
import com.cegeka.alarmtest.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class UpdateActivity extends Activity {
	private ListView listView;
	private static ArrayList<Alarm> alarmen = new ArrayList<Alarm>();
	private Button updateButton;
	private Button logOutButton;
	private TextView textViewOffline;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		initWidgets();
	}

	private void initWidgets() {
		listView = (ListView) findViewById(R.id.listViewAlarms);
		updateButton = (Button) findViewById(R.id.updateButton);
		logOutButton = (Button) findViewById(R.id.logOutButton);
		textViewOffline = (TextView) findViewById(R.id.textViewOffline);
		initListview(alarmen);
		initializeListOnShow();
		if(!InternetChecker.isNetworkAvailable(this) || UserLoaderSaver.loadUser(this)==null){
			updateButton.setVisibility(View.INVISIBLE);
			logOutButton.setVisibility(View.INVISIBLE);
			textViewOffline.setText("You are viewing the alarms offline.");
			textViewOffline.setTextSize(17);
		}
	}

	/**
	 * Initialize the list the first time with alarms already in the database withot updating.
	 */
	private void initializeListOnShow(){
		AlarmsDataSource alarmDB =new AlarmsDataSource(this);
		alarmDB.open();
		try {
			alarmen =alarmDB.getAllAlarms();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}finally{
			alarmDB.close();
		}

		initListview(alarmen);

	}

	/**
	 * Initialize the list of alarms.
	 * @param alarms The alarms the list should show.
	 */
	private void initListview(ArrayList<Alarm> alarms) {
		ArrayAdapter<Alarm> arrayAdapter = new ArrayAdapter<Alarm>(this,android.R.layout.simple_list_item_1, alarms);
		listView.setAdapter(arrayAdapter); 
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				final Alarm alarm = (Alarm) listView.getItemAtPosition(position);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						final AlertDialog alertDialog = new AlertDialog.Builder(UpdateActivity.this).create();
						alertDialog.setTitle(alarm.getTitle());
						alertDialog.setMessage(alarm.getFullInformation());
						alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								alertDialog.cancel();
							}
						});
						//alertDialog.setIcon(R.drawable.ic_launcher);
						alertDialog.show();
					}
				});
			}
		});
	}

	public void updateAlarms(View view){
		if(InternetChecker.isNetworkAvailable(this)){
			final ProgressDialog myPd_ring=ProgressDialog.show(UpdateActivity.this, "Please wait", "Loading please wait..", true);
			myPd_ring.setCancelable(true);
			new UpdateThread(myPd_ring).start();
		}else{
			redirectToMainActivity();
			finish();
		}
	}

	public void logOut(View view){
		try {
			UserLoaderSaver.deleteUser(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		getAlarmen().clear();
		redirectToMainActivity();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
	}


	public ArrayList<Alarm> getAlarmen() {
		return alarmen;
	}

	public void setAlarmen(ArrayList<Alarm> alarmen) {
		UpdateActivity.alarmen = alarmen;
	}

	private void redirectToMainActivity() {
		Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
		startActivity(intent);
	}


	//*************************INNER CLASSES*************************************

	private class UpdateThread extends Thread{
		private ProgressDialog dialog;

		public UpdateThread(ProgressDialog progressDialog){
			this.dialog=progressDialog;
		}

		@Override
		public void run() {
			try
			{
				alarmen.clear();
				ArrayList<com.cegeka.alarmmanager.connection.model.Alarm> alarmsFromDB = null;
				WebServiceConnector connector = new WebServiceConnector();
				User u;
				try {
					u = UserLoaderSaver.loadUser(UpdateActivity.this);
					alarmsFromDB = connector.getAlarmsFromUser(u);
//					alarmsFromDB.removeAll(Collections.singleton(null));
					for(com.cegeka.alarmmanager.connection.model.Alarm a : alarmsFromDB){
						Alarm alarm = AlarmScheduleController.convertAlarm(a);
						alarmen.add(alarm);
					}
					alarmen.removeAll(Collections.singleton(null));
					AlarmScheduleController.updateAlarms(alarmen, UpdateActivity.this);
					runInitializeList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			dialog.dismiss();
		}

		private void runInitializeList() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					initListview(alarmen);
				}
			});
		}
	}

}
