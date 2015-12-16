package dv512.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.javadocmd.simplelatlng.LatLng;

import dv512.model.Event;
import dv512.model.dao.EventsDAO;

@Named
@ViewScoped
public class FeedController implements Serializable {
	private static final long serialVersionUID = 1127731622673465704L;

	@Inject
	private EventsDAO events;
	
	private List<Event> feed;
	
	
	@Inject
	private LoginController thisUser;

	
	public List<Event> getEvents() {
		return feed;
	}

	
	public String joinEvent() {
		
		return null;		
	}
	
	public String leaveEvent() {
		
		
		return null;
	}
	
	
	public void loadData() {
		feed = events.feed(thisUser.getUserId(), 
				new LatLng(56.8, 14.8), 100);
	}
	
}
