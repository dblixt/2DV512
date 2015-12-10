package dv512.controller;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Dog;
import dv512.model.Profile;
import dv512.model.User;
import dv512.model.service.UserService;

@Named
@RequestScoped
public class ProfileController {
	
	@Inject 
	private LoginController thisUser;
	
	@Inject
	private UserService userService;
		
	private User userProfile;

	
	private String viewUserId = null;
	
	
	public String getId() {
		return viewUserId;
	}
	
	public void setId(String id) {
		if(id != null) {
			viewUserId = id;
		}		
	}
	
	public Profile getProfile() {
		if(userProfile != null) {
			return userProfile.getProfile();
		}
		
		return null;
	}
	
	public List<Dog> getDogs() {
		if(userProfile != null) {
			return userProfile.getProfile().getDogs();
		}
		return null;
	}
	
	
	public void loadData() {
		if(viewUserId == null) {
			setId(thisUser.getUserId());
		}
		
		if(userProfile == null) {
			userProfile = userService.get(viewUserId);	
		}	
	}
	
}
