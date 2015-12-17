package dv512.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Profile;
import dv512.model.dao.service.ProfileService;

@Named
@RequestScoped
public class ProfileController {
	
	@Inject 
	private UserController thisUser;
	
	@Inject
	private ProfileService profileService;
		
	private Profile profile;		
	private int viewUserId = -1;
	
	
	public int getId() {
		return viewUserId;
	}
	
	public void setId(int id) {
		if(id > 0) {
			viewUserId = id;
		}		
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public boolean isEditAllowed() {
		return viewUserId == thisUser.getUserId();
	}
	
	
	public void loadData() {
		if(viewUserId == -1) {
			setId(thisUser.getUserId());
		}
		
		if(profile == null) {
			profile = profileService.load(viewUserId);
		}	
	}
	
}
