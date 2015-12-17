package dv512.controller;


import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.User;
import dv512.model.dao.UsersDAO;


@Named
@ApplicationScoped
public class LoginController implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ACTION_LOGIN_SUCCESS = "success";
	public static final String ACTION_LOGIN_FAIL = "fail";	
	public static final String ACTION_LOGOUT = "logout";
	
	@Inject
	private UserController thisUser;
	
	@Inject
	private UsersDAO userDAO;
	
	private User user;

	private int retryCount = 0;
		
	
	public LoginController() {
		user = new User();
	}
	
	public User getUser(){
		return user;
	}
	
	public int getRetryCount() {
		return retryCount;
	}
	
	public boolean isVerified() {
	    return user.getId() != User.UNKNOWN_ID;
	}
	
	public int getUserId() {
		return user.getId();
	}
	
	public String login() {	
		if(userDAO.verify(user)) {
			retryCount = 0;
			thisUser.setUserId(user.getId());
			return ACTION_LOGIN_SUCCESS;
		}
		else {
			retryCount++;
			return ACTION_LOGIN_FAIL;
		}
	}
	
	public String logout() {
		user.setId(User.UNKNOWN_ID);
		thisUser.setUserId(User.UNKNOWN_ID);
		retryCount = 0;
		user.setEmail(null);
		user.setPassword(null);		
		return ACTION_LOGOUT;
	}
	
}