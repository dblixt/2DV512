package dv512.controller;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Event;
import dv512.model.dao.EventsDAO;

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
		}
	}
	
	public void editEvent(){
		if(event != null) {
			eventDAO.update(event);
		}
	}

	public void cancelEvent() {
		System.out.println("Inside controller cancel");
		if(event != null) {
			System.out.println("Inside controller cancel, found event");
			eventDAO.cancelEvent(event);
		}
	}

}
