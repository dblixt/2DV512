package dv512.controller;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Event;
import dv512.model.Profile;
import dv512.model.dao.EventsDAO;

@Named
@ViewScoped
public class EditEventController implements Serializable {

	private static final long serialVersionUID = 6667656806561372380L;

	@Inject
	private EventsDAO eventDAO;
	@Inject
	private LoginController loginController;
	
	private Event event;
	
	/*id of event if in edit mode*/
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

	public void createEvent() {
		System.out.println("Creating event");
		Profile p = new Profile();
		p.setUserId(loginController.getUserId());
		event.setCreator(p);
		System.out.println(event.getCreator().getUserId());	
		try{
		eventDAO.insert(event);
		System.out.println("Event created");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to create event");
		}
	}
	
	public void loadEvent(){
		if(event == null){
			if(editEventId == -1) {
				event = new Event();
				return;
			}
			event = eventDAO.get(editEventId);
		}
		}
		

	
}

