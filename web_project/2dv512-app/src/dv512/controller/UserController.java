package dv512.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Profile;
import dv512.model.User;
import dv512.model.dao.ProfilesDAO;

@Named
@SessionScoped
public class UserController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private ProfilesDAO profiles;
	
	private int userId = User.UNKNOWN_ID;	
	private Profile profile;
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int id) {
		this.userId = id;
		reload();
	}
		
	public Profile getProfile() {
		return profile;
	}
	
	
	// should contain basic information about signed in user.
	// e.g. profile object (no dogs), notification...
	
	// this should be injected in other controllers instead of
	// LoginController.
	
	public void reload() {
		if(userId != -1) {
			profile = profiles.get(userId);
		}
		else {
			profile = null;
		}
	}
}
