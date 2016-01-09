package dv512;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Profile;
import dv512.model.User;
import dv512.model.dao.NotificationsDAO;
import dv512.model.dao.ProfilesDAO;

@Named
@SessionScoped
public class UserSession implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ACTION_LOGOUT = "logout";
	
	@Inject
	private ProfilesDAO profiles;
	
	@Inject
	private NotificationsDAO notifications;
		
	private int userId = User.UNKNOWN_ID;	
	private Profile profile;
	
	private TimeZone timeZone;
	
	private long lastNotificationCountUpdateTime = 0;
	private int notificationCount = 0;
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int id) {
		this.userId = id;
		reload();
	}
			
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
		
		// time zone test
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		dateFormat.setTimeZone(timeZone);
		Date date = new Date();		
		System.out.println("Date: " + dateFormat.format(date));
	}
	
	public TimeZone getTimeZone() {
		return timeZone;
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public int getNotificationCount() {
		if(Instant.now().getEpochSecond() - lastNotificationCountUpdateTime > 10) {
			lastNotificationCountUpdateTime = Instant.now().getEpochSecond();
			notificationCount = notifications.count(getUserId());
		}
				
		return notificationCount;
	}
	
	public boolean isValid() {
		return userId != User.UNKNOWN_ID;
	}
	
	
	public void reload() {
		if(userId != -1) {
			profile = profiles.get(userId);
			lastNotificationCountUpdateTime = 0;
			getNotificationCount();
		}
		else {
			profile = null;
		}
	}
		
	public String logout() {		
		// Invalidate current session.
		FacesContext.getCurrentInstance()
			.getExternalContext().invalidateSession();
		
		userId = User.UNKNOWN_ID;
		profile = null;
		timeZone = null;
		lastNotificationCountUpdateTime = 0;
		notificationCount = 0;
		return ACTION_LOGOUT;
	}
	
	@PreDestroy
	private void onDestroy() {
		System.out.println("User session detroyed.");
		logout();		
	}
}
