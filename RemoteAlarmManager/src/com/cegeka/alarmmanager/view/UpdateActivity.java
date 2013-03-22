package com.cegeka.alarmmanager.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.cegeka.alarmmanager.AlarmScheduleController;
import com.cegeka.alarmmanager.connection.UserLoaderSaver;
import com.cegeka.alarmmanager.connection.WebServiceConnector;
import com.cegeka.alarmmanager.connection.model.User;
import com.cegeka.alarmmanager.db.Alarm;
import com.cegeka.alarmtest.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UpdateActivity extends Activity {
	private ListView listView;
	private static ArrayList<Alarm> alarmen = new ArrayList<Alarm>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		initWidgets();
	}

	private void initWidgets() {
		listView = (ListView) findViewById(R.id.listViewAlarms);
		try {
			initListview(alarmen);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initListview(ArrayList<Alarm> alarms) throws FileNotFoundException {
		ArrayAdapter<Alarm> arrayAdapter = new ArrayAdapter<Alarm>(this,android.R.layout.simple_list_item_1, alarms);
		listView.setAdapter(arrayAdapter); 
	}

	public void updateAlarms(View view){
		if(isNetworkAvailable()){
			final ProgressDialog myPd_ring=ProgressDialog.show(UpdateActivity.this, "Please wait", "Loading please wait..", true);
			myPd_ring.setCancelable(true);
			new UpdateThread(myPd_ring).start();
		}else{
			redirectToMainActivity();
			finish();
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
					alarmsFromDB.removeAll(Collections.singleton(null));
					for(com.cegeka.alarmmanager.connection.model.Alarm a : alarmsFromDB){
						Alarm alarm = AlarmScheduleController.convertAlarm(a);
						alarmen.add(alarm);
					}
					alarmen.removeAll(Collections.singleton(null));
					AlarmScheduleController.scheduleAlarms(alarmen, UpdateActivity.this);
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
					try {
						initListview(alarmen);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

}
