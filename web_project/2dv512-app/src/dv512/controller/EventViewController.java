package dv512.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

	private static final long serialVersionUID = 6667656806561372380L;

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
	private UserController userController;

	private Event event;
	private Comment comment;
	private int eventId = -1;
	private int mode;

	private List<Profile> profiles = new ArrayList<>();
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

	public int getMode() {
		System.out.println("Mode is: " + mode);
		return mode;
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

	public Profile getProfile() {

		return event.getCreator();
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public List<Dog> getDogs() {
		return dogs;
	}

	public String joinEvent() {
		if (eventsDAO.join(userController.getUserId(), event.getId())) {
			event.setJoinStatus(Event.JOIN_STATUS_JOIN_REQUESTED);

			Notification n = Notification.create(Notification.TYPE_REQUEST_JOIN, userController.getUserId(),
					event.getCreator().getUserId(), event.getId());

			notificationsDAO.insert(n);
		}
		return null;
	}

	public String leaveEvent() {
		if (eventsDAO.leave(userController.getUserId(), event.getId())) {
			event.setJoinStatus(Event.JOIN_STATUS_UNJOINED);
		}
		return null;
	}

	public boolean isOwnEvent() {
		return event.getCreator().getUserId() == userController.getUserId();
	}

	public void loadData() {

		System.out.println("eventId: " + eventId);

		if (eventId != -1) {
			if (!dataLoaded) {

				event = eventsDAO.eventView(eventId, userController.getUserId());

				System.out.println("event.getJoinStatus() " + event.getJoinStatus());

				System.out.println(event.getId());
				System.out.println(event.getLatitude());
				System.out.println(event.getLongitude());

				comments = commentsDAO.listAll(eventId);
				dogs = dogsDAO.listAllEvent(eventId);
				profiles.add(event.getCreator());
				profiles.addAll(profilesDAO.listAllEvent(eventId));

				if (comment == null) {
					comment = new Comment();
					comment.setUserId(userController.getUserId());
					comment.setEventId(eventId);
				}

				dataLoaded = true;

			}
			
			else{
				comments = commentsDAO.listAll(eventId);
			}
		}

	}

	public void getProfileFromComment() {

	}

	public String saveData() {

		// save changes to database.
		comment.setDate(System.currentTimeMillis());
		commentsDAO.insert(comment);

		// create notifications
		for (Profile p : profiles) {

			if (p.getUserId() != comment.getUserId()) {
				Notification n = Notification.create(Notification.TYPE_COMMENT_POSTED, comment.getUserId(),
						p.getUserId(), event.getId());
				notificationsDAO.insert(n);
			}

		}

		// remove old comment
		comment = new Comment();
		comment.setUserId(userController.getUserId());
		comment.setEventId(eventId);

		return null;
	}

}
