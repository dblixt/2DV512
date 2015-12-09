package dv512.controller;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.nosql.Dog;
import dv512.model.nosql.Profile;
import dv512.model.nosql.User;
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
		return userProfile.getProfile();
	}
	
	public List<Dog> getDogs() {
		return userProfile.getProfile().getDogs();
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
