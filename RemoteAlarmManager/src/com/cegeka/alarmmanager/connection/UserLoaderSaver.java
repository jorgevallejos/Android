package com.cegeka.alarmmanager.connection;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

import com.cegeka.alarmmanager.connection.model.User;


public class UserLoaderSaver
{

	/**
	 *  Save the {@link User} from the {@link SharedPreferences}
	 * 
	 * @param user The {@link User} to be saved in the {@link SharedPreferences}.
	 * @throws IOException
	 */
	public static void saveUser(Context ctx, User user) throws IOException
	{
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("id", user.getId());
		editor.putString("naam", user.getPaswoord());
		editor.putString("achternaam", user.getEmailadres());
		editor.putString("email", user.getEmailadres());
		editor.putString("paswoord", user.getPaswoord());
		editor.commit();
	}


	/**
	 * Load the {@link User} from the {@link SharedPreferences}
	 * 
	 * @return A {@link User} if the user was saved else null
	 */
	public static User loadUser(Context ctx)
	{
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);
		
		if(!(settings.contains("id") || settings.contains("naam") || settings.contains("achternaam") || settings.contains("email") || settings.contains("paswoord")))
		{
			return null;
		}
		int id = settings.getInt("id", 0);
		String naam = settings.getString("naam", "");
		String achternaam = settings.getString("achternaam", "");
		String email = settings.getString("email", "");
		String paswoord = settings.getString("paswoord", "");
		
		User user = new User(id, naam, achternaam, email);
		user.setPaswoord(paswoord);
		
		return user;
	}
	
	/**
	 * Delete the User from the {@link SharedPreferences}.
	 * @param ctx The {@link Context}.
	 * @throws IOException
	 */
	public static void deleteUser(Context ctx) throws IOException
	{
		SharedPreferences settings = ctx.getSharedPreferences("file", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}
	
}
