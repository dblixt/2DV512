package dv512.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.javadocmd.simplelatlng.LatLng;

import dv512.UserSession;
import dv512.model.Event;
import dv512.model.Notification;
import dv512.model.Profile;
import dv512.model.dao.EventsDAO;
import dv512.model.dao.NotificationsDAO;
import dv512.model.dao.ProfilesDAO;

@Named
@ViewScoped
public class FeedController implements Serializable {
	private static final long serialVersionUID = 1127731622673465704L;

	@Inject
	private UserSession session;
	
	@Inject
	private EventsDAO events;
	
	@Inject 
	private ProfilesDAO profiles;
	
	@Inject
	private NotificationsDAO notifications;
	
	
	private Profile profile;
	private List<Event> feed;
	
	
	public List<Event> getEvents() {
		return feed;
	}

	
	public String joinEvent(Event e) {
		if(events.join(session.getUserId(), e.getId(), false)) {		
			e.setJoinStatus(Event.JOIN_STATUS_JOIN_REQUESTED);			

			Notification n = Notification.create(
					Notification.TYPE_REQUEST_JOIN, 
					session.getUserId(), 
					e.getCreator().getUserId(), 
					e.getId());
				
			notifications.insert(n);			
		}		
		return null;		
	}
	
	public String leaveEvent(Event e) {		
		if(events.leave(session.getUserId(), e.getId())) {
			e.setJoinStatus(Event.JOIN_STATUS_UNJOINED);		
		}		
		return null;
	}
	
	
	public boolean isOwnEvent(int creatorId) {
		return creatorId == session.getUserId();
	}
	
	
	public void loadData() {	
		if(feed == null) {
			System.out.println("FeedController: loading data");
			profile = profiles.get(session.getUserId());
			feed = events.feed(session.getUserId(), 
					new LatLng(profile.getLatitude(),
					profile.getLongitude()), profile.getRadius());
		}		
	}
	
}
