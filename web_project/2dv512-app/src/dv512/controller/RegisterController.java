package dv512.controller;

import java.io.Serializable;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.NosqlManager;
import dv512.model.nosql.User;
import dv512.model.service.UserService;

@Named
@ViewScoped
public class RegisterController implements Serializable {

	private static final long serialVersionUID = -5690292807686490605L;

	public static final String ACTION_REGISTER_SUCCESS = "success";
	public static final String ACTION_REGISTER_FAIL = "fail";

	private final int DEFAULT_MODE = 0;
	private final int REGISTER_MODE = 1;
	private final int SUCCESS_MODE = 2;
	private final int FAILED_MODE = 3;

	private int mode = DEFAULT_MODE;

	private User user;
	
	@Inject
	private UserService userService;

	
	public RegisterController() {
		user = new User();
	}
	
	
	
	public User getUser() {
		return user;
	}

	public int getMode() {
		System.out.println("Mode is: " + mode);
		return mode;
	}

	public void switchMode(AjaxBehaviorEvent event) {
		System.out.println("Switching Mode");
		if (mode == DEFAULT_MODE) {
			mode = REGISTER_MODE;
			return;
		}
		if (mode == REGISTER_MODE) {
			mode = DEFAULT_MODE;
			return;
		}
		if (mode == FAILED_MODE) {
			mode = REGISTER_MODE;
		}
	}

	public void register() {
		System.out.println("Register user: " + user.getEmail());
		
		try {
			userService.create(user);
			mode = SUCCESS_MODE;
			return;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
				
		mode = FAILED_MODE;
	}

}
