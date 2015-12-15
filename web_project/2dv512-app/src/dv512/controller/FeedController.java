package dv512.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Event;
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

	
	public List<Event> getEvents() {
		return new ArrayList<>();
	}

	
	
	public void loadData() {
			
	}
	
}
