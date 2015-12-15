package dv512.model;

import java.util.ArrayList;
import java.util.List;

public class Event {

	private int id;

	private String title;
	private String description;
	private long date;		
	private double longitude;
	private double latitude;

	private double distance;
	
	private User creator;	
	private List<User> joins = new ArrayList<>();
	
		
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
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

	
	public List<User> getJoins() {
		return joins;
	}

	public void setJoins(List<User> joins) {
		this.joins = joins;
	}

}
