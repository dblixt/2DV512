package dv512;


import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class LoginController implements Serializable {
	private static final long serialVersionUID = 1610404137333266630L;

	public static final String ACTION_VERIFY_SUCCESS = "success";
	public static final String ACTION_VERIFY_FAIL = "fail";	
	public static final String ACTION_LOGOUT = "logout";
	
	private boolean verified = false;
	private int retryCount = 0;

	private String email;
	private String password;
	
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	

	public int getRetryCount() {
		return retryCount;
	}
	
	
	public boolean isVerified() {
	    return verified;
	}
	
	public String login() {
		if("admin".equals(email) && "admin".equals(password)) {
			verified = true;
			setPassword(null);; // don't store it.
			
			return ACTION_VERIFY_SUCCESS;
		}
		
		retryCount++;
		return ACTION_VERIFY_FAIL;
	}
	
	public String logout() {
		verified = false;
		setEmail(null);
		return ACTION_LOGOUT;
	}
	
}
