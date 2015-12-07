package dv512.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.dao.DogsDAO;
import dv512.dao.ProfilesDAO;
import dv512.model.Dog;
import dv512.model.Profile;
import dv512.model.User;

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
	private List<Dog> dogs = new ArrayList<Dog>();
	
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
	
	public List<Dog> getDogs() {
		return dogs;
	}
	
	
	public void loadData() {
		if(viewUserId == -1) {
			setId(thisUser.getUserId());
		}
		
		if(profile == null) {
			profile = profilesDAO.get(thisUser.getUserId());		
			dogs = dogsDAO.listAll(thisUser.getUserId());			
		}	
	}
	


}
