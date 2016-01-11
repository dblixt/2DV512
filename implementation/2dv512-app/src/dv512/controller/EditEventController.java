package dv512.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.UserSession;
import dv512.model.Event;
import dv512.model.Notification;
import dv512.model.Profile;
import dv512.model.dao.EventsDAO;
import dv512.model.dao.NotificationsDAO;

@Named
@ViewScoped
public class EditEventController implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String SUCCESS = "success";
	private final String FAIL = "fail";

	@Inject
	private EventsDAO eventDAO;

	@Inject
	private UserSession session;

	@Inject
	private NotificationsDAO notificationsDAO;

	private Event event;

	/** id of event if in edit mode, -1 otherwise */
	private int editEventId = -1;

	public Event getEvent() {
		return event;
	}

	/**
	 * Get id of event to edit if in edit mode.
	 * @return id of event if in edit mode, -1 otherwise.
	 */
	public int getEditEventId() {
		return editEventId;
	}

	public void setEditEventId(int eventId) {
		editEventId = eventId;
	}

	
	public void loadEvent() {
		if ((event == null || editEventId != event.getId()) && session.getProfile() != null) {
			if (editEventId == -1) {
				event = new Event();
				event.setLatitude(session.getProfile().getLatitude());
				event.setLongitude(session.getProfile().getLongitude());
			}
			else {
				event = eventDAO.get(editEventId, -1);
				
				if(event == null || event.getCreator().getUserId() != session.getUserId() || event.isCanceled()) {
					// something is fishy, force error message.
					event = null;
				}
			}			
		}
	}
	
	public String createEvent() {
		event.setCreator(session.getProfile());
		try {
			eventDAO.insert(event);
			return SUCCESS;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return FAIL;
		}
	}

	public String editEvent() {
		if (event != null) {
			if(eventDAO.update(event)) {
				List<Profile> attendants = eventDAO.getJoins(event.getId());				
				List<Notification> notifications = new ArrayList<>();
				
				for (Profile p : attendants) {
					if(p.getUserId() != session.getUserId()) {
						Notification n = Notification.create(
								Notification.TYPE_EVENT_UPDATED, 
								session.getUserId(),
								p.getUserId(), 
								event.getId()
						);
						notifications.add(n);
					}				
				}
				
				notificationsDAO.insert(notifications);
			}
			
		}
		
		return SUCCESS;
	}

	public String cancelEvent() {
		if (event != null) {
			if(eventDAO.cancel(event)) {
				List<Profile> attendants = eventDAO.getJoins(event.getId());				
				List<Notification> notifications = new ArrayList<>();
				
				for (Profile p : attendants) {
					if(p.getUserId() != session.getUserId()) {
						Notification n = Notification.create(
								Notification.TYPE_EVENT_CANCELLED, 
								session.getUserId(),
								p.getUserId(), 
								event.getId()
						);
						notifications.add(n);
					}				
				}
				
				notificationsDAO.insert(notifications);
			}
		}
		
		return SUCCESS;
	}

}
