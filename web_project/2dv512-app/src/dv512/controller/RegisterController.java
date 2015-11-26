package dv512.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.DbManager;

@Named
@SessionScoped
public class RegisterController implements Serializable{

	private static final long serialVersionUID = -5690292807686490605L;
	public static final String ACTION_REGISTER_SUCCESS = "success";
	public static final String ACTION_REGISTER_FAIL = "fail";

	private String name;
	private String password;
	private String email;
	
	
	@Inject
	private DbManager dbManager;

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String register() {
		Connection con = null;
		PreparedStatement s = null;
		
		try {			
			con = dbManager.getConnection();
			
			s = con.prepareStatement("INSERT INTO USERS(NAME, EMAIL, PASSWORD) VALUES(?,?,?)");
			
			s.setString(1, name);
			s.setString(2, email);
			s.setString(3, password);
			s.executeUpdate();
			System.out.println("User added to DB");
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			//Hantera exception
			return ACTION_REGISTER_FAIL;
		}
		finally {
			dbManager.close(con);
			dbManager.close(s);
		}
		
		return ACTION_REGISTER_SUCCESS;
		
		
	}

}
