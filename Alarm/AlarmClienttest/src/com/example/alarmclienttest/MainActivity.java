package com.example.alarmclienttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button requestButton;
	private Button alarmsButton;
	private WebServiceConnector connector = new WebServiceConnector();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		requestButton = (Button) findViewById(R.id.requestButton);
		alarmsButton = (Button) findViewById(R.id.alarmsButton);
		initHandlers();
	}

	private void initHandlers() {
		requestButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(connector.getUser("david.s.maes@gmail.com"));
			}
		});

		alarmsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(connector.getAlarmsFromUser("david.s.maes@gmail.com"));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
