package dv512.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dv512.model.Event;
import dv512.model.User;

@Named
@ViewScoped
public class FeedController implements Serializable {
	private static final long serialVersionUID = 1127731622673465704L;

	@Inject
	private LoginController thisUser;
	
	
	//private List<FeedEvent> events;
	
	
	public List<Event> getEvents() throws JsonProcessingException, IOException {
		
		System.out.println(new ObjectMapper().writeValueAsString(new User()));
		
		
		return null;
	}
	
	
	
	public void loadData() {
		//events = new ArrayList<>();
		
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
