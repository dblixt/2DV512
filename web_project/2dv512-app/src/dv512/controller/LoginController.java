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


@Named
@SessionScoped
public class LoginController implements Serializable {
	private static final long serialVersionUID = 1610404137333266630L;

	public static final String ACTION_LOGIN_SUCCESS = "success";
	public static final String ACTION_LOGIN_FAIL = "fail";	
	public static final String ACTION_LOGOUT = "logout";
	

	@Inject
	private DbManager dbManager;
	
	
	private int userId = -1;
	private String email;
	private String password;
	
	private int retryCount = 0;
	

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
	    return userId != -1;
	}
	
	public int getUserId() {
		return userId;
	}

	
	public String login() {	
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT id FROM Users WHERE email = ? AND password = ?");
			stmt.setString(1, email);
			stmt.setString(2, password);
			
			ResultSet r = stmt.executeQuery();
			if(r != null && r.next()) {
				System.out.println("User verification succeded!");
				userId = r.getInt("id");
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
		userId = -1;
		retryCount = 0;
		setEmail(null);
		setPassword(null);
		return ACTION_LOGOUT;
	}
	
}
