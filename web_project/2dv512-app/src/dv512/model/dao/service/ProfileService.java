package dv512.model.dao.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Profile;
import dv512.model.dao.DogsDAO;
import dv512.model.dao.ProfilesDAO;

@Named
@ApplicationScoped
public class ProfileService {

	@Inject
	private ProfilesDAO profiles;
	
	@Inject
	private DogsDAO dogs;
	
	
	/**
	 * Load a profile from database, including dogs.
	 * @return
	 */
	public Profile load(int userId) {		
		Profile p = profiles.get(userId);
		if(p != null) {
			p.setDogs(dogs.listAll(userId));
		}
					
		return p;
	}
	
	
	
	
}
