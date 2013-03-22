package com.cegeka.alarmmanager.view;

import java.io.IOException;

import com.cegeka.alarmmanager.connection.UserLoaderSaver;
import com.cegeka.alarmmanager.connection.WebServiceConnector;
import com.cegeka.alarmmanager.connection.model.User;
import com.cegeka.alarmmanager.exceptions.WebserviceException;
import com.cegeka.alarmtest.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private EditText emailEditText;
	private EditText paswoordEditText;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initWidgets();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	private void initWidgets() {
		emailEditText = (EditText) findViewById(R.id.emailtextBox);
		paswoordEditText = (EditText) findViewById(R.id.paswoordTextBox);
	}

	public void login(View view){
		if(isNetworkAvailable()){
			final ProgressDialog myPd_ring=ProgressDialog.show(LoginActivity.this, "Please wait", "Loading please wait..", true);
			myPd_ring.setCancelable(true);
			new Thread(new Runnable() {  
				@Override
				public void run() {
					doLogin();
					myPd_ring.dismiss();
				}


			}).start();
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
	private void doLogin() {
		String email = emailEditText.getText().toString();
		String pass = paswoordEditText.getText().toString();

		final WebServiceConnector connector = new WebServiceConnector();
		User u = null;
		try {
			u = connector.getUser(email, pass);
			if(u!=null){
				System.out.println(u);
				u.setPaswoord(pass);
				try {
					UserLoaderSaver.saveUser(LoginActivity.this, u);
					redirectToUpdateActivity();
					finish();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
						alertDialog.setTitle("Login failed.");
						alertDialog.setMessage("Your username or password is incorrect. Try again.");
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
		} catch (WebserviceException e1) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
					alertDialog.setTitle("Login failed.");
					alertDialog.setMessage("The connection timed out. Check your internet connection.");
					//alertDialog.setIcon(R.drawable.ic_launcher);
					alertDialog.show();
				}
			});
		}
	}

	private void redirectToUpdateActivity() {
		Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
		startActivity(intent);
	}

	private void redirectToMainActivity() {
		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
	}

}
