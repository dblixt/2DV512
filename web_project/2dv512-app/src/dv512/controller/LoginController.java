package dv512.controller;


import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.User;
import dv512.model.service.UserService;


@Named
@ApplicationScoped
public class LoginController implements Serializable {
	private static final long serialVersionUID = 1610404137333266630L;

	public static final String ACTION_LOGIN_SUCCESS = "success";
	public static final String ACTION_LOGIN_FAIL = "fail";	
	public static final String ACTION_LOGOUT = "logout";
	
	@Inject 
	private UserService userService;
	
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
	    return user.getId() != null;
	}
	
	public String getUserId() {
		return user.getId();
	}
	
	public String login() {	
		User result = userService.verify(user);
		if(result != null) {
			user = result;
			retryCount = 0;
			return ACTION_LOGIN_SUCCESS;
		}
		else {
			retryCount++;
			return ACTION_LOGIN_FAIL;
		}
	}
	
	public String logout() {
		user = new User();
		retryCount = 0;
		return ACTION_LOGOUT;
	}
	
}