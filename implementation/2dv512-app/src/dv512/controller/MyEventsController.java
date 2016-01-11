package dv512.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.javadocmd.simplelatlng.LatLng;

import dv512.UserSession;
import dv512.model.Event;
import dv512.model.Profile;
import dv512.model.dao.EventsDAO;
import dv512.model.dao.ProfilesDAO;

@Named
@ViewScoped
public class MyEventsController implements Serializable{
	
	private static final long serialVersionUID = -2423088749569717310L;

	@Inject
	private UserSession session;
	
	@Inject
	private EventsDAO events;
	
	@Inject 
	private ProfilesDAO profiles;
	
	private Profile profile;
	private List<Event> myEvents;
	
	
	public List<Event> getEvents() {
		return myEvents;
	}
	
	public boolean isOwnEvent(int creatorId) {
		return creatorId == session.getUserId();
	}
	
	public void loadData() {	
		if(myEvents == null) {
			profile = profiles.get(session.getUserId());
			
			if(profile != null) {
				myEvents = events.listMyEvents(session.getUserId(), 
						new LatLng(profile.getLatitude(),
						profile.getLongitude()), profile.getRadius());
			}		
		}		
	}
}
