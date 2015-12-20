package dv512.controller;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.javadocmd.simplelatlng.LatLng;

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
	private UserController thisUser;
	
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
		if(events.join(thisUser.getUserId(), e.getId(), 
				Instant.now().getEpochSecond())) {
			
			e.setJoinStatus(Event.JOIN_STATUS_JOIN_REQUESTED);			
			System.out.println("Joined event: " + e.getId());		
			
			Notification n = new Notification();
			n.setType(Notification.TYPE_REQUEST_JOIN);
			n.getTargetUser().setUserId(e.getCreator().getUserId());
			n.getSourceUser().setUserId(thisUser.getUserId());
			n.getEvent().setId(e.getId());			
			notifications.insert(n);			
		}		
		return null;		
	}
	
	public String leaveEvent(Event e) {		
		if(events.leave(thisUser.getUserId(), e.getId())) {
			e.setJoinStatus(Event.JOIN_STATUS_UNJOINED);		
		}		
		return null;
	}
	

	public boolean isOwnEvent(int creatorId) {
		return creatorId == thisUser.getUserId();
	}
	
	
	
	public void loadData() {	
		if(feed == null) {
			System.out.println("FeedController: loading data");
			profile = profiles.get(thisUser.getUserId());
			feed = events.feed(thisUser.getUserId(), 
					new LatLng(profile.getLatitude(), profile.getLongitude()), 100);
		}		
	}
	
}
