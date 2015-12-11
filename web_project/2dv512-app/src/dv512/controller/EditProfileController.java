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

import dv512.controller.util.FileUploadHandler;
import dv512.controller.util.FileUploadHandler.FileUploadListener;
import dv512.controller.util.ImgUtils;
import dv512.model.Dog;
import dv512.model.Profile;
import dv512.model.User;
import dv512.model.service.ImageService;
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
	
	@Inject
	private ImageService imageService;
	
	private User user;
	private List<String> pendImgDel = new ArrayList<>();
	

	@PostConstruct
	private void init() {		
		// set file upload handler to take care of incoming files.
		fileUploadHandler.setFileUploadListener(new FileUploadListener() {			
			@Override
			public void onUploadFile(String filename, InputStream is) {						
				InputStream resizedIs = ImgUtils.scaleImage(is);
						
				if(user.getProfile().getImage() != null) {
					pendImgDel.add(user.getProfile().getImage());
				}
						
				String id = imageService.create(resizedIs);
				user.getProfile().setImage(id);	
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
		// if desired.
		
		Iterator<Dog> itr = getProfile().getDogs().iterator();
		while(itr.hasNext()) {
			Dog d = itr.next();
			if(d.getId() == id) {
				itr.remove();
			}
		}
	}
	

	public void loadData() {
		if(user == null) {
			System.out.println("Loading profile data!");
			user = userService.get(thisUser.getUserId());
		}
	}
	
	public String saveData() {		
		userService.update(user);
		imageService.delete(pendImgDel);
		
		return "profile.xhtml?faces-redirect=true";
	}
	
}
