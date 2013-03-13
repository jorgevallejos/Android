package com.example.dwaaldetectie;

public class GeoPoint {

	private int latitudeE6;
	private int longitudeE6;
	
	public GeoPoint(int latitudeE6, int longitude) {
		this.latitudeE6=latitudeE6;
		this.longitudeE6=longitude;
	}

	public double getLatitudeE6() {
		// TODO Auto-generated method stub
		return latitudeE6;
	}

	public double getLongitudeE6() {
		return longitudeE6;
	}

}
