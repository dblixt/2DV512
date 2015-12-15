package dv512.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Profile;
import dv512.model.dao.DogsDAO;
import dv512.model.dao.ProfilesDAO;

@Named
@RequestScoped
public class ProfileController {
	
	@Inject 
	private DogsDAO dogsDAO;	
	@Inject 
	private ProfilesDAO profilesDAO;
	
	@Inject 
	private LoginController thisUser;
	
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
	
	public void loadData() {
		if(viewUserId == -1) {
			setId(thisUser.getUserId());
		}
		
		if(profile == null) {
			profile = profilesDAO.get(thisUser.getUserId());	
			profile.setDogs(dogsDAO.listAll(thisUser.getUserId()));		
		}	
	}
	


}
