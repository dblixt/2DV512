package dv512.model;

public class Comment {
	
	private int id;
	private int eventId;
	private int userId;
	private String comment;
	private long date;
	private Profile profile;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getEventId() {
		return eventId;
	}
	
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public long getDate() {
		return date;
	}
	
	public void setDate(long date) {
		this.date = date;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
		
	}
	
	public Profile getProfile(){
		return profile;
	}
	
}
