package dv512.model.nosql;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

public class User extends CouchDbDocument {
	
	private static final long serialVersionUID = 1L;
	
	@TypeDiscriminator // email is unique field for user documents. No other document will contain this field.
	private String email;	
	private String password;
	
	private Profile profile;

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
