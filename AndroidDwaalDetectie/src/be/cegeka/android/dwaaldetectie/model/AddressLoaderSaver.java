package be.cegeka.android.dwaaldetectie.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import android.os.Environment;
import android.util.Log;

public class AddressLoaderSaver {

	/**
	 * Save the numbers. The numbers file will be completely overwritten.
	 * @param selectedNumbers An {@link ArrayList} of Strings representing the numbers that need to be saved.
	 * @throws IOException
	 */
	public static void saveAddress(String adres) throws IOException{
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but all we need
			//  to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}

		if(mExternalStorageAvailable && mExternalStorageWriteable){
			// Create a path where we will place our private file on external
			File root = Environment.getExternalStorageDirectory(); 
			File dir = new File (root.getAbsolutePath() + "/savefiles");
			dir.mkdirs();
			File file = new File(dir, "myData.txt");
			try {
				PrintWriter writer = new PrintWriter(file);
				writer.append(adres);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				Log.w("ExternalStorage", "Error writing " + file, e);
			}

		}
	}

	/**
	 * Load all the numbers saved by this app.
	 * @return An {@link ArrayList} of the cell-phone numbers.
	 */
	public static String loadNumbers() throws Exception{
		try{
		Scanner scanner = null;
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/savefiles/myData.txt");
			scanner = new Scanner(file);
		
		
		return scanner.nextLine();
		}catch (Exception e) {
			throw e;
		}
	}
}
