package dv512.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	private int fail = 0;
	
	
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
	
	public int getFail() {
		return fail;
	}

	public String register() {
		Connection con = null;
		PreparedStatement s = null;
		
		try {			
			con = dbManager.getConnection();
			
			s = con.prepareStatement("INSERT INTO Users(email, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			
			s.setString(1, email);
			s.setString(2, password);
			s.executeUpdate();			
			
			int userId = 0;
			// retrieve auto generated user id.
			ResultSet key = s.getGeneratedKeys();
			if(key.next()) {
				userId = key.getInt("id");
			}
						
			dbManager.close(s);
						
			s = con.prepareStatement("INSERT INTO Profiles(user_id,name) VALUES(?,?)");
			s.setInt(1, userId);
			s.setString(2, name);
			
			s.executeUpdate();		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			fail = 1;
			return ACTION_REGISTER_FAIL;
		}
		finally {
			dbManager.close(con);
			dbManager.close(s);
		}
		fail = 0;
		return ACTION_REGISTER_SUCCESS;
	}

}
