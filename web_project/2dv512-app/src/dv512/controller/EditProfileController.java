package dv512.controller;

import java.io.InputStream;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ektorp.AttachmentInputStream;

import dv512.controller.util.FileUploadHandler;
import dv512.controller.util.FileUploadHandler.FileUploadListener;
import dv512.controller.util.ImgUtils;
import dv512.model.nosql.Profile;
import dv512.model.nosql.User;
import dv512.model.service.UserService;

@Named
@ViewScoped
public class EditProfileController implements Serializable {

	private static final long serialVersionUID = -43584334356821659L;
	
	@Inject 
	private FileUploadHandler fileUploadHandler;
		
	@Inject 
	private LoginController thisUser;
	
	@Inject
	private UserService userService;
	
	
	private User user;

	//private List<Dog> pendDogDel = new ArrayList<>();
	//private List<String> pendImgDel = new ArrayList<>();
	
	@PostConstruct
	private void init() {		
		// set file upload handler to take care of incoming files.
		fileUploadHandler.setFileUploadListener(new FileUploadListener() {			
			@Override
			public void onUploadFile(String filename, InputStream is) {						
				InputStream resizedIs = ImgUtils.scaleImage(is);
				
				AttachmentInputStream ais = new AttachmentInputStream(
						"profile_pic", resizedIs, ImgUtils.IAMGE_MIME_TYPE
				);
				
				userService.createAttachment(user, ais);
				
				
				/*
				
				File path = ImgUtils.createPath(ImgUtils.TYPE_PROFILE_PIC, thisUser.getUserId());
				
				if(ImgUtils.saveImage(path, is)) {
					// add old profile img to pending deletes.
					pendImgDel.add(profile.getProfilePic());
					
					// set new profile image, not saved in db until saveData() is called.
					profile.setProfilePic(path.getName());			
				}	
				*/			
			}
		});
	}

	public User getUser() {
		return user;
	}
	
	public Profile getProfile() {
		if(user != null) {
			return user.getProfile();
		}
		return null;
	}
		
	public void removeDog(int id) {
		// add do list of pending dog deletions, won't actually happen
		// until saveData is called, which gives the user a chance to abort
		// id desired.
		
		
		
		
		/*Iterator<Dog> itr = dogs.iterator();
		while(itr.hasNext()) {
			Dog d = itr.next();
			if(d.getId() == id) {
				pendDogDel.add(d);
				itr.remove();
			}
		}*/
	}
	

	public void loadData() {
		if(user == null) {
			System.out.println("Loading profile data!");
			user = userService.get(thisUser.getUserId());
		}
	}
	
	public String saveData() {		
		userService.update(user);
		
		/*
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
		*/
		
		return "profile.xhtml?faces-redirect=true";
	}
	
}
