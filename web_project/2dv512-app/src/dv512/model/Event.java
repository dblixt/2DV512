package dv512.model;

public class Event {

	private int id;

	private String title;
	private String description;
	private long date;		
	private double longitude;
	private double latitude;

	private boolean isJoined;
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


	public boolean isJoined() {
		return isJoined;
	}
	

	public void setJoined(boolean isJoined) {
		this.isJoined = isJoined;
	}

}
