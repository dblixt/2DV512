package dv512.model.nosql;

import java.util.Random;

public class Dog {
	
	private int id;
	
	private String name;
	private String gender;
	private int age;
	private String breed;
	private String picture;
	
	private String image;
	
	public static final int generateId() {
		return new Random().nextInt(1000 * 1000);
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getBreed() {
		return breed;
	}
	
	public void setBreed(String breed) {
		this.breed = breed;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}		
		else if(obj instanceof Dog) {
			Dog o = (Dog) obj;
			return this.id == o.id;
		}
		
		return false;
	}

}
