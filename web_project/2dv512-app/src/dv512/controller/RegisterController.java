package dv512.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class RegisterController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5690292807686490605L;
	public static final String ACTION_REGISTER_SUCCESS = "success";
	public static final String ACTION_REGISTER_FAIL = "fail";

	private String name;
	private String password;
	private String email;

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
		System.out.println("Inside Register");
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			Connection con = DriverManager.getConnection("jdbc:db2://5.10.125.192:50000/SQLDB", "user03014",
					"qN9iWXYTQlBr");

			// Connection c = myDataSource.getConnection();
			PreparedStatement s = con.prepareStatement("INSERT INTO USERS(NAME, EMAIL, PASSWORD) VALUES(?,?,?)");
			
			s.setString(1, name);
			s.setString(2, email);
			s.setString(3, password);
			s.executeUpdate();
			System.out.println("User added to DB");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			return ACTION_REGISTER_FAIL;
		}
		return ACTION_REGISTER_SUCCESS;
	}

}
