package dv512.controller;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.UserSession;
import dv512.model.Notification;
import dv512.model.dao.EventsDAO;
import dv512.model.dao.NotificationsDAO;

@Named
@ViewScoped
public class NotificationsController implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject
	private UserSession session;
	
	@Inject
	private NotificationsDAO notifications;
	
	@Inject 
	private EventsDAO events;
	
	
	private List<Notification> notificationList;
	
	public List<Notification> getNotifications() {
		return notificationList;
	}
	
	
	public void approveJoin(Notification n) {
		if(events.approveJoin(n.getSourceUser().getUserId(), n.getEvent().getId())) {			
			// send notification to tell that request has been approved.
			Notification approved = Notification.create(
					Notification.TYPE_JOIN_APPROVED, 
					session.getUserId(),
					n.getSourceUser().getUserId(), 
					n.getEvent().getId());
			
			notifications.insert(approved);			
			remove(n);
		}
	}
	
	public void denyJoin(Notification n) {
		// remove user from event joins.
		if(events.leave(n.getSourceUser().getUserId(), n.getEvent().getId())) {
			remove(n);
		}		
	}
	
	public void remove(Notification n) {
		if(notifications.remove(n.getId())) {
			Iterator<Notification> itr = notificationList.iterator();
			while(itr.hasNext()) {
				Notification i = itr.next();
				if(i.getId() == n.getId()) {
					itr.remove();
				}
			}
			session.refreshNotificationCount(true);
		}
	}
	
	
	public void loadData() {
		if(notificationList == null) {
			System.out.println("Loading notifications");
			notificationList = notifications.listAll(session.getUserId());
		}	
	}
	
}
