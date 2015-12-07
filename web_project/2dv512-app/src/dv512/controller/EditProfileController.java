package dv512.controller;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.FileUploadHandler;
import dv512.controller.util.ImgUtils;
import dv512.controller.util.FileUploadHandler.FileUploadListener;
import dv512.dao.DogsDAO;
import dv512.dao.ProfilesDAO;
import dv512.model.Dog;
import dv512.model.Profile;
import dv512.model.User;

@Named
@ViewScoped
public class EditProfileController implements Serializable {

	private static final long serialVersionUID = -43584334356821659L;
	
	@Inject 
	private ProfilesDAO profilesDAO;
	@Inject 
	private DogsDAO dogsDAO;	
		
	@Inject 
	private FileUploadHandler fileUploadHandler;
		
	@Inject 
	private LoginController thisUser;
	
	
	private Profile profile;
	private List<Dog> dogs = new ArrayList<>();
	
	private List<Dog> pendDogDel = new ArrayList<>();
	private List<String> pendImgDel = new ArrayList<>();
	
	@PostConstruct
	private void init() {		
		// set file upload handler to take care of incoming files.
		fileUploadHandler.setFileUploadListener(new FileUploadListener() {			
			@Override
			public void onUploadFile(String filename, InputStream is) {				
				File path = ImgUtils.createPath(ImgUtils.TYPE_PROFILE_PIC, thisUser.getUserId());
				
				if(ImgUtils.saveImage(path, is)) {
					// add old profile img to pending deletes.
					pendImgDel.add(profile.getProfilePic());
					
					// set new profile image, not saved in db until saveData() is called.
					profile.setProfilePic(path.getName());			
				}				
			}
		});
	}
	
	
	public Profile getProfile() {
		return profile;
	}
	
	public List<Dog> getDogs() {
		return dogs;
	}
	
	
	public void removeDog(int id) {
		// add do list of pending dog deletions, won't actually happen
		// until saveData is called, which gives the user a chance to abort
		// id desired.
		Iterator<Dog> itr = dogs.iterator();
		while(itr.hasNext()) {
			Dog d = itr.next();
			if(d.getId() == id) {
				pendDogDel.add(d);
				itr.remove();
			}
		}
	}
	

	public void loadData() {
		if(profile == null) {
			System.out.println("Loading profile data!");
			profile = profilesDAO.get(thisUser.getUserId());		
			dogs = dogsDAO.listAll(thisUser.getUserId());			
		}
	}
	
	public String saveData() {
		// save changes to database.
		profilesDAO.update(profile);
		
		// remove deleted dogs from database.
		for(Dog d : pendDogDel) {
			dogsDAO.delete(d.getId());
		}
		
		// remove ghost profile pics from storage.
		for(String name : pendImgDel) {
			ImgUtils.delete(name, ImgUtils.TYPE_PROFILE_PIC);
		}
		
		return "profile.xhtml?faces-redirect=true";
	}
	
}
