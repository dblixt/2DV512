package dv512.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.DbManager;

@Named
@SessionScoped
public class RegisterController implements Serializable{

	private static final long serialVersionUID = -5690292807686490605L;
	
	public static final String ACTION_REGISTER_SUCCESS = "success";
	public static final String ACTION_REGISTER_FAIL = "fail";
	
	private final int DEFAULT_MODE = 0;
	private final int REGISTER_MODE = 1;
	private final int FAILED_MODE = 2;
	
	private int mode = DEFAULT_MODE;
	
	private String name;
	private String password;
	private String email;

		
	
	@Inject
	private DbManager dbManager;

	public void setName(String name) {
		System.out.println(name);
		this.name = name;
	}

	public void setPassword(String password) {
		System.out.println(password);
		this.password = password;
	}

	public void setEmail(String email) {
		System.out.println(email);
		this.email = email;
	}

	public void setMode(int mode) {
		this.mode = mode;
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
	
	
	public int getMode() {
		//System.out.println("Getting Mode " + mode);
		return mode;
	}

	public void switchmode(AjaxBehaviorEvent event) {
		//System.out.println("Inside switchMode");
		if(mode == DEFAULT_MODE){
			mode = REGISTER_MODE;
			System.out.println("Mode = " +mode);
			return;
		}
		if(mode == REGISTER_MODE){
			mode = DEFAULT_MODE;
			System.out.println("Mode = " +mode);
		}
	}

	public String register() {
		Connection con = null;
		PreparedStatement s = null;
		System.out.println("Trying to add user");
		//System.out.println(name +"  "+email+ " "+password);
		
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
			mode = FAILED_MODE;
			return ACTION_REGISTER_FAIL;
		}
		finally {
			dbManager.close(con);
			dbManager.close(s);
		}
		mode = DEFAULT_MODE;
		System.out.println("User added");
		return ACTION_REGISTER_SUCCESS;
	}

}
