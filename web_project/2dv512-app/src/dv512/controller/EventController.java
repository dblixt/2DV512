package dv512.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.dao.EventsDAO;
import dv512.model.Comment;
import dv512.model.Event;
import dv512.model.User;


@Named
@ViewScoped
public class EventController implements Serializable {

	private static final long serialVersionUID = 6667656806561372380L;

	
	@Inject 
	private EventsDAO eventsDAO;	
	
	@Inject 
	private User thisUser;
	
	private Event event;
	
	private List<Comment> comments = new ArrayList<>();
	
	public Event event() {
		return event;
	}
	
	public void loadData() {
		if(event == null) {
			System.out.println("Loading profile data!");
			event = eventsDAO.get(thisUser.getId());			
		}
	}
	
	public String saveData() {
		// save changes to database.
		eventsDAO.update(event);
		
				
		return "profile.xhtml?faces-redirect=true";
	}
	

}
