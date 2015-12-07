package dv512.controller;


import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.dao.UserDAO;
import dv512.model.User;


@Named
@ApplicationScoped
public class LoginController implements Serializable {
	private static final long serialVersionUID = 1610404137333266630L;

	public static final String ACTION_LOGIN_SUCCESS = "success";
	public static final String ACTION_LOGIN_FAIL = "fail";	
	public static final String ACTION_LOGOUT = "logout";
	

	@Inject
	private UserDAO userDAO;
	
	
	private User user = new User();

	private int retryCount = 0;
		
	public User getUser(){
		return user;
	}
	
	public int getRetryCount() {
		return retryCount;
	}
	
	public boolean isVerified() {
	    return user.getId() != -1;
	}
	
	public int getUserId() {
		return user.getId();
	}
	
	public String login() {	
		boolean userDOAResponse = userDAO.get(user);
		if(userDOAResponse == true){
			retryCount = 0;
			return ACTION_LOGIN_SUCCESS;
		}
		else {
			retryCount++;
			return ACTION_LOGIN_FAIL;
		}
	}
	
	public String logout() {
		user.setId(-1);
		retryCount = 0;
		user.setEmail(null);
		user.setPassword(null);
		return ACTION_LOGOUT;
	}
	
}
