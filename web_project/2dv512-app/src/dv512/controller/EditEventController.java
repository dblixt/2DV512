package dv512.controller;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Event;
import dv512.model.Event.User;
import dv512.model.service.EventService;

@Named
@ViewScoped
public class EditEventController implements Serializable {

	private static final long serialVersionUID = 6667656806561372380L;

	@Inject
	private EventService eventService;
	@Inject
	private LoginController loginController;
	
	private Event event = new Event();
	
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
		//createTestData();		
		event.setCreator(User.from(loginController.getUser()));
		try{
		eventService.create(event);
		System.out.println("Event created");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to create event");
		}
	}
	
	public void loadEvent(){
		if(editEventId != -1){
			event = new Event();
		}
		//event = eventService.get(eventId);
	}

	
}

