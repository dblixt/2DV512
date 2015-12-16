package dv512.model;

import java.util.ArrayList;
import java.util.List;

public class Profile {

	private int userId;
	private String name;
	private String gender;
	private String description;

	private double longitude;
	private double latitude;

	private String image;

	private List<Dog> dogs;

	public Profile() {
		dogs = new ArrayList<>();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int id) {
		this.userId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<Dog> getDogs() {
		return dogs;
	}

	public void setDogs(List<Dog> dogs) {
		this.dogs = dogs;
	}

	public void addDog(Dog dog) {
		if (dogs == null) {
			dogs = new ArrayList<>();
		}
		dogs.add(dog);
	}

}
