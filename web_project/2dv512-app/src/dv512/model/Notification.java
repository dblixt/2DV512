package dv512.model;

public class Notification {

	public static final int TYPE_REQUEST_JOIN = 1;
	public static final int TYPE_JOIN_APPROVED = 2;
	public static final int TYPE_EVENT_UPDATED = 3;
	public static final int TYPE_EVENT_CANCELLED = 4;
	public static final int TYPE_COMMENT_POSTED = 5;
	
	
	private int id;
	private int type;
	private Profile sourceUser;
	private Profile targetUser;
	private Event event;
	private long date;
	
	public Notification() {
		sourceUser = new Profile();
		targetUser = new Profile();
		setEvent(new Event());
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
	
	
	public Profile getSourceUser() {
		return sourceUser;
	}
	
	public void setSourceUser(Profile p) {
		sourceUser = p;
	}
	
	public Profile getTargetUser() {
		return targetUser;
	}
	
	public void setTargetUser(Profile p) {
		targetUser = p;
	}

	
	
	
	public boolean isTypeJoinRequest() {
		return type == TYPE_REQUEST_JOIN;
	}
	
	public boolean isTypeJoinApproved() {
		return type == TYPE_JOIN_APPROVED;
	}

	
	
}
