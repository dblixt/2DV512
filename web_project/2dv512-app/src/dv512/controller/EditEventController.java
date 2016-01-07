package dv512.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Event;
import dv512.model.Notification;
import dv512.model.dao.EventsDAO;
import dv512.model.dao.NotificationsDAO;

@Named
@ViewScoped
public class EditEventController implements Serializable {

	private static final long serialVersionUID = 6667656806561372380L;
	private final String CREATE_SUCESS = "success";
	private final String CREATE_FAILED = "fail";

	@Inject
	private EventsDAO eventDAO;

	@Inject
	private UserController userController;

	@Inject
	private NotificationsDAO notificationsDAO;

	private Event event;

	/* id of event if in edit mode */
	private int editEventId = -1;

	public Event getEvent() {
		return event;
	}

	public int getEditEventId() {
		return editEventId;
	}

	public void setEditEventId(int eventId) {
		editEventId = eventId;
	}

	public String createEvent() {
		System.out.println("Creating event");
		event.setCreator(userController.getProfile());
		try {
			eventDAO.insert(event);
			System.out.println("Event created");
			return CREATE_SUCESS;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to create event");
			return CREATE_FAILED;
		}
	}

	public void loadEvent() {
		if (event == null) {
			if (editEventId == -1) {
				event = new Event();
				event.setLatitude(userController.getProfile().getLatitude());
				event.setLongitude(userController.getProfile().getLongitude());
				return;
			}
			event = eventDAO.get(editEventId);
			if (event.getCreator().getUserId() != userController.getUserId()) {
				editEventId = -1;
				event = new Event();
				event.setLatitude(userController.getProfile().getLatitude());
				event.setLongitude(userController.getProfile().getLongitude());
			}
		}
	}

	public void editEvent() {
		if (event != null) {
			List<Integer> userList = eventDAO.update(event);
			for (int userId : userList) {
				Notification n = Notification.create(Notification.TYPE_EVENT_UPDATED, event.getCreator().getUserId(),
						userId, event.getId());
				notificationsDAO.insert(n);
			}
		}
	}

	public void cancelEvent() {
		if (event != null) {
			List<Integer> userList = eventDAO.cancelEvent(event);
			for (int userId : userList) {
				Notification n = Notification.create(Notification.TYPE_EVENT_CANCELLED, event.getCreator().getUserId(),
						userId, event.getId());
				notificationsDAO.insert(n);
			}
		}
	}

}
