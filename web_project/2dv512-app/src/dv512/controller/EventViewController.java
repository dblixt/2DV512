package dv512.controller;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.UserSession;
import dv512.model.Comment;
import dv512.model.Dog;
import dv512.model.Event;
import dv512.model.Notification;
import dv512.model.Profile;
import dv512.model.dao.CommentsDAO;
import dv512.model.dao.DogsDAO;
import dv512.model.dao.EventsDAO;
import dv512.model.dao.NotificationsDAO;
import dv512.model.dao.ProfilesDAO;

@Named
@ViewScoped
public class EventViewController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private NotificationsDAO notificationsDAO;

	@Inject
	private EventsDAO eventsDAO;

	@Inject
	private ProfilesDAO profilesDAO;

	@Inject
	private CommentsDAO commentsDAO;

	@Inject
	private DogsDAO dogsDAO;

	@Inject
	private UserSession session;

	private Event event;
	private Comment comment;
	private int eventId = -1;


	private List<Profile> attendants = new ArrayList<>();
	private List<Comment> comments = new ArrayList<>();
	private List<Dog> dogs = new ArrayList<>();

	private boolean dataLoaded = false;

	// public void switchMode(AjaxBehaviorEvent event) {
	// System.out.println("Switching Mode" + event.toString());
	// mode++;
	// if (mode == 3) {
	// mode = 0;
	// }
	// }

	public void setId(int id) {
		System.out.println("setId " + id);
		eventId = id;
	}

	public int getId() {
		return eventId;
	}

	public Event getEvent() {
		return event;
	}

	public Comment setComment() {
		return comment;
	}

	public Comment getComment() {
		return comment;
	}


	public List<Profile> getProfiles() {
		return attendants;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public List<Dog> getDogs() {
		return dogs;
	}

	public String joinEvent() {
		if (eventsDAO.join(session.getUserId(), event.getId())) {
			event.setJoinStatus(Event.JOIN_STATUS_JOIN_REQUESTED);

			Notification n = Notification.create(Notification.TYPE_REQUEST_JOIN, session.getUserId(),
					event.getCreator().getUserId(), event.getId());

			notificationsDAO.insert(n);
		}
		return null;
	}

	public String leaveEvent() {
		if (eventsDAO.leave(session.getUserId(), event.getId())) {
			event.setJoinStatus(Event.JOIN_STATUS_UNJOINED);
		}
		return null;
	}

	public boolean isOwnEvent() {
		return event.getCreator().getUserId() == session.getUserId();
	}

	public void loadData() {
		System.out.println("eventId: " + eventId);

		if (eventId != -1) {
			if (!dataLoaded) {

				event = eventsDAO.eventView(eventId, session.getUserId());

				System.out.println("event.getJoinStatus() " + event.getJoinStatus());

				System.out.println(event.getId());
				System.out.println(event.getLatitude());
				System.out.println(event.getLongitude());

				comments = commentsDAO.listAll(eventId);
				dogs = dogsDAO.listAllForEvent(eventId);
				attendants.add(event.getCreator());
				attendants.addAll(profilesDAO.listAllEvent(eventId));

				if (comment == null) {
					comment = new Comment();
				}

				dataLoaded = true;
			}		
			else {
				comments = commentsDAO.listAll(eventId);
			}
		}

	}

	
	public String postComment() {
		// save changes to database.
		comment.setUserId(session.getUserId());
		comment.setEventId(eventId);
		comment.setDate(Instant.now().getEpochSecond());
		commentsDAO.insert(comment);

		// create notifications
		List<Notification> notifications = new ArrayList<>();		
		for (Profile p : attendants) {
			if (p.getUserId() != comment.getUserId()) {
				Notification n = Notification.create(
						Notification.TYPE_COMMENT_POSTED, 
						comment.getUserId(),
						p.getUserId(), event.getId());
				notifications.add(n);
			}
		}
		
		// batch insert
		notificationsDAO.insert(notifications);
		
		// clear visible body.
		comment.setBody(null);
		return null;
	}

}
