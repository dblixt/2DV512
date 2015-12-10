package dv512.controller;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.FileUploadHandler;
import dv512.controller.util.FileUploadHandler.FileUploadListener;
import dv512.controller.util.ImgUtils;
import dv512.model.Dog;
import dv512.model.Image;
import dv512.model.User;
import dv512.model.service.ImageService;
import dv512.model.service.UserService;

@Named
@ViewScoped
public class EditDogController implements Serializable {

	private static final long serialVersionUID = -1036810508598748155L;

	@Inject 
	private FileUploadHandler fileUploadHandler;
	
	@Inject
	private LoginController session;
	
	@Inject
	private UserService userService;
	@Inject
	private ImageService imageService;
	
	
	/** Id of the dog to edit if in edit mode. */
	private int editDogId = -1;
	
	private User user;
	private Dog dog;	
	private List<String> pendImgDel = new ArrayList<>();
	
	
	@PostConstruct
	private void init() {
		// set file upload handler to take care of incoming files.
		fileUploadHandler.setFileUploadListener(new FileUploadListener() {
			@Override
			public void onUploadFile(String filename, InputStream is) {
				System.out.println("upload dog callback.");
				
				InputStream resizedIs = ImgUtils.scaleImage(is);
				
				if(dog.getImage() != null) {
					pendImgDel.add(dog.getImage());
				}
						
				String id = imageService.create(resizedIs);
				dog.setImage(id);
			}
		});
	}

	
	public int getId() {
		return editDogId;
	}
	
	public void setId(int id) {
		editDogId = id;
	}

	public Dog getDog() {
		return dog;
	}
	
	public void setDog(Dog dog) {
		this.dog = dog;
	}
	
	
	public void loadData() {
		if(user == null) {
			user = userService.get(session.getUserId());
			
			if(user != null) {
				if(editDogId == -1) {
					dog = new Dog();
					dog.setId(Dog.generateId());
					user.getProfile().addDog(dog);
					return;
				}
				
				List<Dog> dogs = user.getProfile().getDogs();
				
				for(Dog d : dogs) {
					if(editDogId == d.getId()) {
						dog = d;
						return;
					}
				}
				
				// if still null, then dog for id did not exist.
				// switch to add mode.
				if(dog == null) {
					setId(-1);
					dog = new Dog();
					dog.setId(Dog.generateId());
					user.getProfile().addDog(dog);
				}
			}			
		}
	}
	
	public String saveData() {		
		userService.update(user);		
		// TODO: remove images.		
		
		
		
		
		return "editprofile.xhtml?faces-redirect=true";
		
	}
		
}
