package dv512.model;

public class Dog {
	
	private int id;
	private int userId;
	
	private String name;
	private String gender;
	private int age;
	private String breed;
	
	private String image;
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
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
