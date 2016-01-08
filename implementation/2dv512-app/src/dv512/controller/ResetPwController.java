package dv512.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.dao.UsersDAO;

@Named
@ViewScoped
public class ResetPwController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private UsersDAO users;
	
	
	private String token;
	private String newPassword;
		
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setPassword(String password) {
		newPassword = password;
	}
	
	public String getPassword() {
		return null;
	}
	
	
	public String resetPassword() {
		if(token != null && token.length() == 32) {
			users.updatePassword(token, newPassword);
		}		
		return "index?faces-redirect=true";
	}
	
}
