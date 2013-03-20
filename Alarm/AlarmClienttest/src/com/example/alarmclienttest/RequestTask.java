package com.example.alarmclienttest;

import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import be.cegeka.model.Alarm;
import be.cegeka.model.User;



public class RequestTask extends AsyncTask<String, String, String>{
	//METHOD NAME
	private static String METHOD_NAME = "getAlarmsFromUser";
	// NAMESPACE/METHOD
	private static String SOAP_ACTION = "http://cegeka.be/getAlarmsFromUser";
	//TARGETNAMESPACE IN WSDL
	private static final String NAMESPACE = "http://cegeka.be/";
	//URL OF WSDL FILE 
	private static final String URL = "http://172.31.207.79:8080/AlarmWebService/AlarmWebService";

	public static String GET_USER = "getUser";
	public static String GET_ALARMS_FROM_USER = "getAlarmsFromUser";

	public static boolean done = true;
	public static User user;
	public static ArrayList<Alarm> alarms = new ArrayList<Alarm>();

	@Override
	protected String doInBackground(String... uri) {
		String responseString = null;
		SOAP_ACTION = "http://cegeka.be/"+uri[0];
		METHOD_NAME = uri[0];
		try {
			if(METHOD_NAME.equals(GET_USER)){
				String naam = uri[1];
				soapGetUser(naam);
			}
			if(METHOD_NAME.equals(GET_ALARMS_FROM_USER)){
				String naam = uri[1];
				soapGetAlarmsFromUser(naam);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return responseString;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		//Do anything with response..
	}


	public void soapGetUser(String username) throws IOException, XmlPullParserException {
		done=false;
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);          
		request.addProperty("emailadres", username);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(URL);
		User u=null;
		try {
			ht.call(SOAP_ACTION, envelope);
			SoapObject response = (SoapObject)envelope.getResponse();
			
			String achternaam = response.getProperty("achternaam").toString();
			String email = response.getProperty("emailadres").toString();
			int id = Integer.parseInt(response.getProperty("id").toString());
			String naam = response.getProperty("naam").toString();
			u = new User(id,naam,achternaam,email);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		user= u;
		done=true;
	}
	
	public void soapGetAlarmsFromUser(String username) throws IOException, XmlPullParserException {
		done=false;
		ArrayList<Alarm> alarms = new ArrayList<Alarm>();
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);          
		request.addProperty("emailadres", username);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		HttpTransportSE ht = new HttpTransportSE(URL);
		
		try {
			ht.call(SOAP_ACTION, envelope);
			SoapObject response = (SoapObject)envelope.bodyIn;
			for(int i= 0; i< response.getPropertyCount(); i++){
		        SoapObject o = (SoapObject) response.getProperty(i); 
				String info = o.getProperty("info").toString();
				int id = Integer.parseInt(o.getProperty("id").toString());
				Alarm a = new Alarm();
				a.setId(id);
				a.setTitle(o.getProperty("title").toString());
				a.setInfo(info);
				a.setRepeated(Boolean.parseBoolean(o.getProperty("repeated").toString()));
				a.setRepeatUnit(o.getProperty("repeatUnit").toString());
				a.setRepeatQuantity(Integer.parseInt(o.getProperty("repeatQuantity").toString()));
				a.setDate(Long.parseLong(o.getProperty("date").toString()));
				a.setRepeatEndDate(Long.parseLong(o.getProperty("repeatEndDate").toString()));
				alarms.add(a);
		    }
			for(Alarm a : alarms){
				System.out.println(a.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestTask.alarms=alarms;
		done=true;
	}



}