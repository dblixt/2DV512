package dv512.controller;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.UserSession;
import dv512.controller.util.FileUploadHandler;
import dv512.controller.util.FileUploadHandler.FileUploadListener;
import dv512.controller.util.ImgUtils;
import dv512.model.Dog;
import dv512.model.Profile;
import dv512.model.dao.DogsDAO;
import dv512.model.dao.ImagesDAO;
import dv512.model.dao.ProfilesDAO;

@Named
@ViewScoped
public class EditProfileController implements Serializable {

	private static final long serialVersionUID = -43584334356821659L;
		
	@Inject 
	private UserSession session;	
	@Inject 
	private ProfilesDAO profilesDAO;	
	@Inject 
	private DogsDAO dogsDAO;	
	@Inject
	private ImagesDAO imagesDAO;
		
	@Inject 
	private FileUploadHandler fileUploadHandler;
		
	private Profile profile;
	
	private List<Dog> pendDogDel = new ArrayList<>();
	private List<String> pendImgDel = new ArrayList<>();
	
	@PostConstruct
	private void init() {		
		// set file upload handler to take care of incoming files.
		fileUploadHandler.setFileUploadListener(new FileUploadListener() {			
			@Override
			public void onUploadFile(String filename, InputStream is) {				
				InputStream resizedIs = ImgUtils.scaleImage(is);
				
				if(profile.getImage() != null) {
					pendImgDel.add(profile.getImage());
				}
						
				String id = imagesDAO.create(resizedIs);
				profile.setImage(id);	
			}
		});
	}
	
	
	public Profile getProfile() {
		return profile;
	}

	
	public void removeDog(int id) {
		// add do list of pending dog deletions, won't actually happen
		// until saveData is called, which gives the user a chance to abort
		// id desired.
		Iterator<Dog> itr = profile.getDogs().iterator();
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
			profile = profilesDAO.get(session.getUserId());		
			profile.setDogs(dogsDAO.listAll(session.getUserId()));			
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
		imagesDAO.delete(pendImgDel);
		
		// reload data in user controller since it has changed.
		session.reload();
		
		return "profile.xhtml?faces-redirect=true";
	}
	
}
