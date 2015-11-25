package dv512.controller;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import dv512.model.DbManager;

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
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DbManager.getConnection();
			stmt = con.prepareStatement("SELECT email FROM Users WHERE email = ? AND password = ?");
			stmt.setString(1, email);
			stmt.setString(2, password);
			
			ResultSet r = stmt.executeQuery();
			if(r != null && r.next()) {
				System.out.println("User verification succeded!");
				verified = true;
				return ACTION_VERIFY_SUCCESS;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			DbManager.close(stmt);
			DbManager.close(con);
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
