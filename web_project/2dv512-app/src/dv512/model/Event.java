package dv512.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Event {

	public static final int JOIN_STATUS_UNJOINED = 0;
	public static final int JOIN_STATUS_JOIN_REQUESTED = 1;
	public static final int JOIN_STATUS_JOINED = 2;
	
	
	private int id;

	private String title;
	private String description;
	private long date;
	private String dateUTC;
	private double longitude;
	private double latitude;

	private int joinStatus;
	private double distance;
	
	private Profile creator;	
	
	
	public Event() {
		creator = new Profile();
	}
			
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Profile getCreator() {
		return creator;
	}

	public void setCreator(Profile creator) {
		this.creator = creator;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;	
	}


	public int getJoinStatus() {
		return joinStatus;
	}
	

	public void setJoinStatus(int status) {
		this.joinStatus = status;
	}
	
	public String getDateUTC(){
		Date formatDate = new Date(0);
		System.out.println(date);
		System.out.println(formatDate);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		formatDate.setTime(date * 1000);
		System.out.println(formatDate);
		dateUTC = formatDate.toString();
		
		dateUTC = dateUTC.replace(":00 UTC", "");
		System.out.println(dateUTC);
		return dateUTC;
	}
}
