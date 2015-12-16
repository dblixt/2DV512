package dv512.model;

public class User{	
	
	public static final int UNKNOWN_ID = -1;
	
	private int id = UNKNOWN_ID;
	private String email;	
	private String password;
	
	private Profile profile;

	
	public User() {
		profile = new Profile();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
}
