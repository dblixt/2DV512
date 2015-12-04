package dv512.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import dv512.DbManager;
import dv512.ProfilePicUtils;

@Named
@ViewScoped
public class Profile implements Serializable {

	private static final long serialVersionUID = 9105883453098941489L;
	

	private int userId;
	
	private String name;
	private String gender;
	private String description;

	private double longitude;
	private double latitude;
	
	private List<Dog> dogs = new ArrayList<>();
		
	private Part uploadFile;
	
	
	private boolean editMode = false;
	private boolean saveNeeded = false;
	
	
	
	@Inject
	private DbManager dbManager;
	
	
	public void setUserId(int id) {
		System.out.println("setting id");
		this.userId = id;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public String getProfilePic() {			
		// prioritize unsaved pics in edit mode.
		if(editMode) {
			File unsaved = ProfilePicUtils.findPicture(userId, true);
			if(unsaved != null) {
				return unsaved.getName();
			}
		}

		File pic = ProfilePicUtils.findPicture(userId, false);
		if(pic != null) {
			return pic.getName();
		}
			
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if(this.name == null || !this.name.equals(name)) {
			saveNeeded = true;
			this.name = name;
		}
	}
	
	public String getGender() {
		System.out.println("get gender: " + gender);
		
		if(gender == null) {
			return "";
		}
		
		return gender; 
	}
	
	public void setGender(String gender) {
		if(this.gender == null || !this.gender.equals(gender)) {
			saveNeeded = true;
			this.gender = gender;
		}
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		if(this.description == null || !this.description.equals(description)) {
			saveNeeded = true;
			this.description = description;
		}
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	
	public List<Dog> getDogs() {
		return dogs;
	}
	
	
	public Part getUploadFile() {
		return uploadFile;
	}
	
	public void setUploadFile(Part file) throws IOException {
		saveNeeded = true;
		// Request to upload new profile pic.
		System.out.println("Upload file: " + file.getSubmittedFileName());

		System.out.println("Current files: " + Arrays.toString(ProfilePicUtils.getFolder().list()));
				
		// clean out any previously uploaded unsaved pictures
		// that might have been left.
		ProfilePicUtils.cleanUnsaved(userId);
					
		FileOutputStream os = null;
		InputStream is = null;
		try {		
			os = new FileOutputStream(ProfilePicUtils
					.createPath(userId, ProfilePicUtils
							.getFileExtension(file.getSubmittedFileName()), true));
			is = file.getInputStream();
			
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = is.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				os.close();
			}
			catch(Exception e) {}
		}
	}
	
	
	public void loadData(boolean editMode) {
		System.out.println("Load profile data for id=" + userId + " edit mode: " + editMode);
		this.editMode = editMode;
		
		// clean out unsaved pictures for this user.
		if(!editMode) {
			ProfilePicUtils.cleanUnsaved(userId);
		}
			
		// laod from database
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT * FROM Profiles WHERE user_id = ?");
			stmt.setInt(1, userId);
		
			ResultSet r = stmt.executeQuery();
			
			if(r != null && r.next()) {
				name = r.getString("name");
				gender = r.getString("gender");
				description = r.getString("description");
				latitude = r.getDouble("pos_lat");
				longitude = r.getDouble("pos_lng");		
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}
		
		
		dogs = new ArrayList<>();
		
		Dog d1 = new Dog();
		d1.setName("Sven");
		d1.setGender("Male");
		d1.setRace("Sheepdog");
		d1.setAge(5);
		d1.setPicture("dog1.jpg");
		
		Dog d2 = new Dog();
		d2.setName("Nils");
		d2.setGender("Male");
		d2.setRace("Fatdog");
		d2.setAge(7);
		d2.setPicture("dog2.jpg");
		
		dogs.add(d1);
		dogs.add(d2);
		

	}
	
	public String saveData() {
		if(saveNeeded) {
			saveNeeded = false;
			
			System.out.println("Profile changed, save needed.");
			
			Connection con = null;
			PreparedStatement stmt = null;
			try {
				con = dbManager.getConnection();
				stmt = con.prepareStatement("UPDATE Profiles SET name = ?, gender = ?, description = ?, pos_lng = ?, pos_lat = ? WHERE user_id = ?");
				stmt.setString(1, name);
				stmt.setString(2, gender);
				stmt.setString(3, description);
				stmt.setDouble(4, longitude);
				stmt.setDouble(5,  latitude);
				stmt.setInt(6, userId);

				stmt.executeUpdate();						
			}
			catch(SQLException e) {
				e.printStackTrace();
				return null;
			}
			finally {
				dbManager.close(stmt);
				dbManager.close(con);
			}		
			
			ProfilePicUtils.saveProfilePicture(userId);		
		}
		else {
			System.out.println("Profile not changed, no save needed.");
		}
		
		return "/user/profile.xhtml?faces-redirect=true&userId=" + userId;
	}
	
	
}
