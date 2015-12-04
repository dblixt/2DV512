package dv512.controller;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.DbManager;
import dv512.model.User;


@Named
@SessionScoped
public class LoginController implements Serializable {
	private static final long serialVersionUID = 1610404137333266630L;

	public static final String ACTION_LOGIN_SUCCESS = "success";
	public static final String ACTION_LOGIN_FAIL = "fail";	
	public static final String ACTION_LOGOUT = "logout";
	

	@Inject
	private DbManager dbManager;
	
	@Inject 
	private User user;
	
	private String password;

	private int retryCount = 0;
	

	public void setEmail(String email) {
		user.setEmail(email);
	}
	
	public String getEmail() {
		return user.getEmail();
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
	    return user.getId() != -1;
	}
	
	public int getUserId() {
		return user.getId();
	}

	
	public String login() {	
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT id FROM Users WHERE email = ? AND password = ?");
			stmt.setString(1, user.getEmail());
			stmt.setString(2, password);
			
			ResultSet r = stmt.executeQuery();
			if(r != null && r.next()) {
				System.out.println("User verification succeded!");
				user.setId(r.getInt("id"));
				retryCount = 0;
				password = null; // do not store it.
				return ACTION_LOGIN_SUCCESS;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}
		
		retryCount++;
		return ACTION_LOGIN_FAIL;
	}
	
	public String logout() {
		user.setId(-1);
		retryCount = 0;
		setEmail(null);
		setPassword(null);
		return ACTION_LOGOUT;
	}
	
}
