package dv512.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.javadocmd.simplelatlng.LatLng;

import dv512.model.Event;
import dv512.model.Event;
import dv512.model.Profile;
import dv512.model.User;
import dv512.model.service.EventService;

@Named
@ViewScoped
public class FeedController implements Serializable {
	private static final long serialVersionUID = 1127731622673465704L;

	@Inject
	private LoginController thisUser;
	
	@Inject
	private EventService eventService;
	
	private List<Event> events;
	
	
	public List<Event> getEvents() throws JsonProcessingException, IOException {
		
	

		return null;
	}
	
	
	
	public void loadData() throws IOException {
		//Profile profile = thisUser.getUser().getProfile();
		//7LatLng origin = new LatLng(profile.getLatitude(), profile.getLongitude());
		
		//eventService.find(origin, 5);
			
		events = new ArrayList<>();
		
		Event e2 = new Event();
		e2.setCreator(Event.User.from(thisUser.getUser()));
		
		
		
		
		
		
		
		
		//FeedEvent e = new FeedEvent();
		
		//Event ev = new Event();
		//ev.setDateTime(System.currentTimeMillis());
		//ev.setDescription("This is a long description of the event that will take place later this week.");
		//ev.setId(1);
		//ev.setTitle("Walk around the Park");
		
		
		
		
			
		//e.setCreator(profilesDAO.get(thisUser.getUserId()));
		//e.setDogs(dogsDAO.listAll(thisUser.getUserId()));
		
		//ev.setUserId(e.getCreator().getUserId());
		
		//e.setDistance(5);
		//e.setJoinCount(3);
		//e.setEvent(ev);
		
		//events.add(e);		
	}
	
}
