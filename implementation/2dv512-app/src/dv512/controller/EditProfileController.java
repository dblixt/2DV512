package dv512.controller;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.SessionScoped;
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
@SessionScoped // don't like it but can't get ajax to work with ConversationScoped. 
public class EditProfileController implements Serializable {
	private static final long serialVersionUID = 1L;
		
	private final static String FILE_TYPE_PROFILE_IMG = "profile";
	private final static String FILE_TYPE_DOG_IMG = "dog";
	
	@Inject 
	private UserSession session;	
	@Inject 
	private ProfilesDAO profilesDAO;	
	@Inject 
	private DogsDAO dogsDAO;	
	@Inject
	private ImagesDAO imagesDAO;
		

	private FileUploadHandler fileUpload;
		
	private Profile profile; // editprofile.xhtml
	
	private int dogId = -1;
	private Dog dog; // dog.xhtml
	
	private List<Dog> pendDogDel = new ArrayList<>();
	private List<String> pendImgDel = new ArrayList<>();
	

	public EditProfileController() {		
		fileUpload = new FileUploadHandler();
		// set file upload handler to take care of incoming files.
		fileUpload.setFileUploadListener(new FileUploadListener() {			
			@Override
			public void onUploadFile(String type, String filename, InputStream is) {	
				System.out.println("Upload : " + type + " file: " + filename);
				
				InputStream resizedIs = ImgUtils.scaleImage(is);
				String id = imagesDAO.create(resizedIs);
				
				System.out.println("File id: " + id + " old: " + profile.getImage());
				
				if(FILE_TYPE_PROFILE_IMG.equals(type) && profile != null) {
					if(profile.getImage() != null) {
						pendImgDel.add(profile.getImage());
					}
					profile.setImage(id);	
				}
				else if(FILE_TYPE_DOG_IMG.equals(type) && dog != null) {
					if(dog.getImage() != null) {
						pendImgDel.add(dog.getImage());
					}
					dog.setImage(id);
				}
			}
		});
	}
		
	public void setDogId(int id) {
		this.dogId = id;
	}
	
	public int getDogId() {
		return dogId;
	}

	
	public Profile getProfile() {
		return profile;
	}

	public Dog getDog() {
		return dog;
	}
		
	public FileUploadHandler getFileUpload() {
		return fileUpload;
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
			}
		}
	}
	
	public String editDog(int id) {
		return "dog.xhtml?faces-redirect=true&id=" + id;
	}
	
	
	public void loadProfile() {
		// make sure no old dog loaded when loading edit profile.
		cancelDog();
		
		if(profile == null) {
			System.out.println("Loading profile data!");
			profile = profilesDAO.get(session.getUserId());		
			profile.setDogs(dogsDAO.listAll(session.getUserId()));			
		}
		else {
			// reload dogs since they may have changed.
			List<Dog> dogs = dogsDAO.listAll(session.getUserId());
			dogs.removeAll(pendDogDel);
			profile.setDogs(dogs);
		}
	}
	
	public String saveProfile() {
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

		cancelProfile(); // clear data, force reload on next visit.
		return "profile.xhtml?faces-redirect=true";
	}

	public String cancelProfile() {
		profile = null;
		return "profile.xhtml?faces-redirect=true";
	}
	
	public void loadDog() {
		if(dog == null) {
			if(dogId == -1) {
				// add new dog mode.
				dog = new Dog();
				dog.setId(-1);
				dog.setUserId(session.getUserId());
				System.out.println("dog null = " + (dog==null));
				return;
			}

			// edit dog mode, load data.
			dog = dogsDAO.get(dogId);
		}		
	}
	
	public String saveDog() {
		if(dog.getId() == -1) {
			dogsDAO.insert(dog);
		}
		else {
			dogsDAO.update(dog);
		}
		
		cancelDog(); // clear data
		return "editprofile.xhtml?faces-redirect=true";
	}
	
	public String cancelDog() {
		dog = null;
		dogId = -1;
		return "editprofile.xhtml?faces-redirect=true";
	}
	
}
