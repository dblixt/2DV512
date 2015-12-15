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
import dv512.model.Event.User;
import dv512.model.Profile;
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
		
		return events;
	}
	
	public List<Event.Dog> buildDogList(Event e) {
		List<Event.Dog> dogs = new ArrayList<Event.Dog>();
		
		for(User u : e.getJoins()) {
			dogs.addAll(u.getDogs());
		}
			
		return dogs;
	}
	
	
	
	public void loadData() throws IOException {
		Profile profile = thisUser.getUser().getProfile();
		LatLng origin = new LatLng(profile.getLatitude(), profile.getLongitude());
		
		events = eventService.find(origin, 5);			
	}
	
}
