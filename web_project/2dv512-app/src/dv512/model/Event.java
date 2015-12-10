package dv512.model;

import java.util.ArrayList;
import java.util.List;

import org.ektorp.support.CouchDbDocument;

//import dv512.model.Comment;

public class Event extends CouchDbDocument {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4462553807977479809L;
	
	private String userId;
	private String date;
	private int hour;
	private int minutes;
	private String title;
	private String description;
	private double longitude;
	private double latitude;
	
	private String DateAndTime;
	
	private List<Profile> profiles = new ArrayList<>();
	//private List<Comment> comments = new ArrayList<>();
	private List<Dog> dogs = new ArrayList<>();

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	/*public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}*/

	public List<Dog> getDogs() {
		return dogs;
	}

	public void setDogs(List<Dog> dogs) {
		this.dogs = dogs;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		System.out.println(date);
		this.date = date;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
	public String DateTimeToString(){
		return date + " "+hour+":"+minutes; 
	}
	

}
