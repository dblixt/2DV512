package dv512.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.dao.DogsDAO;
import dv512.dao.ProfilesDAO;
import dv512.model.FeedEvent;

@Named
@ViewScoped
public class FeedController {

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
		
		e.setCreator(profilesDAO.get(thisUser.getUserId()));
		e.setDogs(dogsDAO.listAll(thisUser.getUserId()));
		
		e.setDistance(5);
		e.setJoinCount(3);
		
		events.add(e);		
	}
	
}
