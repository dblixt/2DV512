package d2v512;


import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = 1610404137333266630L;
	
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
	
	
	
	public String verify() {
		
		
		
		return "fail";
	}
	
	
}
