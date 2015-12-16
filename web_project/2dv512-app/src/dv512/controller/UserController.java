package dv512.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class UserController implements Serializable {
	private static final long serialVersionUID = 1L;

	
	// should contain information about signed in user.
	// e.g. profile object, notification...
	
	// this should be injected in other controllers instead of
	// LoginController.
	
	
	
}
