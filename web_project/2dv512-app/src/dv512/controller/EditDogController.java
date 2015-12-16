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
import dv512.model.dao.DogsDAO;
import dv512.model.dao.ImagesDAO;

@Named
@ViewScoped
public class EditDogController implements Serializable {
	private static final long serialVersionUID = -1036810508598748155L;

	@Inject 
	private DogsDAO dogsDAO;	
	
	@Inject 
	private FileUploadHandler fileUploadHandler;
	
	@Inject
	private LoginController thisUser;
	
	@Inject
	private ImagesDAO imagesDAO;
	
	
	
	/** Id of the dog to edit if in edit mode. */
	private int editDogId = -1;
	
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
						
				String id = imagesDAO.create(resizedIs);
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
		if(dog == null) {
			if(editDogId == -1) {
				// add new dog mode.
				dog = new Dog();
				dog.setId(-1);
				dog.setUserId(thisUser.getUserId());
				return;
			}

			// edit dog mode, load data.
			dog = dogsDAO.get(editDogId);
		}		
	}
	
	public String saveData() {		
		if(dog.getId() == -1) {
			dogsDAO.insert(dog);
		}
		else {
			dogsDAO.update(dog);
		}
		
		// remove ghost profile pics from storage.
		imagesDAO.delete(pendImgDel);
	
		return "editprofile.xhtml?faces-redirect=true";
	}
		
}
