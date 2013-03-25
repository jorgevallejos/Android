package com.cegeka.debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class LogFileWriter {

	public static void writeLogLine(String line){
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
			File file = new File(dir, "AlarmLog.txt");
			try
			{
				FileWriter writer = new FileWriter(file, true);
				Date date = new Date();
				writer.write(date.toString() + ":\t" + line + "\n");
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
