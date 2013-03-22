package com.cegeka.alarmmanager.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import com.cegeka.alarmmanager.connection.UserLoaderSaver;
import com.cegeka.alarmmanager.connection.model.User;
import com.cegeka.alarmtest.R;

public class MainActivity extends Activity {

	private User u = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		u = UserLoaderSaver.loadUser(this);
		if(isNetworkAvailable()){
			if(u==null){
				redirectToLoginActivity();
			}else{
				redirectToUpdateActivity();
			}
			finish();
		}else{
			setContentView(R.layout.activity_main);
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}



	private void redirectToUpdateActivity() {
		Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
		startActivity(intent);
	}

	private void redirectToLoginActivity() {
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
	}
}
