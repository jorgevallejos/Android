package com.cegeka.alarmtest.view;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import android.os.Environment;
import android.util.Log;

import com.cegeka.alarmtest.db.Alarm;

public class DebugWriter {

	public static void writeToFile(List<Alarm> alarms){
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		}
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
		{
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		}
		else
		{
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if (mExternalStorageAvailable && mExternalStorageWriteable)
		{
			// Create a path where we will place our private file on external
			File root = Environment.getExternalStorageDirectory();
			File dir = new File(root.getAbsolutePath() + "/savefiles");
			dir.mkdirs();
			File file = new File(dir, "myData.txt");
			try
			{
				PrintWriter writer = new PrintWriter(file);
				for(Alarm a : alarms){
					writer.append(a.toString() + "\n");
				}
				writer.flush();
				writer.close();
			}
			catch (IOException e)
			{
				Log.w("ExternalStorage", "Error writing " + file, e);
			}

		}
	}
}
