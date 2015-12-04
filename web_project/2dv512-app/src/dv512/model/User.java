package dv512.model;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class User implements Serializable {

	private static final long serialVersionUID = 8626805850252558885L;
	
	private int id = -1;
	private String email;

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
	
}
