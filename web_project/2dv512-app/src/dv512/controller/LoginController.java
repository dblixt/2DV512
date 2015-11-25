package dv512.controller;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.sql.DataSource;

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
	
	
	// For bluemix
	//@Resource(lookup = "jdbc/db2-app-db")
	//private DataSource dataSource;
	
	
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
	
	public String login() throws SQLException, ClassNotFoundException {
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		Connection con = DriverManager.getConnection("jdbc:db2://5.10.125.192:50000/SQLDB", "user03239", "1MDtRJ9K2I72");
			
		//Connection c = myDataSource.getConnection();
		PreparedStatement s = con.prepareStatement("INSERT INTO Users(email, password) VALUES(?,?)");
				
		s.setString(1,  email);
		s.setString(2,  password);
		s.executeUpdate();
		
		
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
