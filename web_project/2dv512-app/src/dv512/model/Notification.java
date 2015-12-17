package dv512.model;

public class Notification {

	public static final int TYPE_REQUEST_JOIN = 1;
	public static final int TYPE_JOIN_APPROVED = 2;
	public static final int TYPE_EVENT_UPDATED = 3;
	public static final int TYPE_EVENT_CANCELLED = 4;
	public static final int TYPE_COMMENT_POSTED = 5;
	
	
	private int id;
	private int type;
	private int receiverUserId; // id of user that has received the notification.
	private int triggerUserId; // id of user that caused notification to be sent.
	private int eventId; // which event this notification is concerning.
	private int commentId; // only for TYPE_COMMENT_POSTED
	
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

	public int getReceiverUserId() {
		return receiverUserId;
	}

	public void setReceiverUserId(int receiverUserId) {
		this.receiverUserId = receiverUserId;
	}

	public int getTriggerUserId() {
		return triggerUserId;
	}

	public void setTriggerUserId(int triggerUserId) {
		this.triggerUserId = triggerUserId;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	
}
