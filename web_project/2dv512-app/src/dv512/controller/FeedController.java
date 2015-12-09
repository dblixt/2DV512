package dv512.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Event;
import dv512.model.FeedEvent;
import dv512.model.dao.DogsDAO;
import dv512.model.dao.ProfilesDAO;

@Named
@ViewScoped
public class FeedController implements Serializable {
	private static final long serialVersionUID = 1127731622673465704L;

	@Inject 
	private ProfilesDAO profilesDAO;
	
	@Inject
	private DogsDAO dogsDAO;
	
	@Inject
	private LoginController thisUser;
	
	
	private List<FeedEvent> events;
	
	
	
	public List<FeedEvent> getEvents() {
		return events;
	}
	
	
	
	public void loadData() {
		events = new ArrayList<>();
		
		FeedEvent e = new FeedEvent();
		
		Event ev = new Event();
		ev.setDateTime(System.currentTimeMillis());
		ev.setDescription("This is a long description of the event that will take place later this week.");
		ev.setId(1);
		ev.setTitle("Walk around the Park");
		
			
		//e.setCreator(profilesDAO.get(thisUser.getUserId()));
		//e.setDogs(dogsDAO.listAll(thisUser.getUserId()));
		
		//ev.setUserId(e.getCreator().getUserId());
		
		e.setDistance(5);
		e.setJoinCount(3);
		e.setEvent(ev);
		
		events.add(e);		
	}
	
}
