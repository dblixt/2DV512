package dv512.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

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

	public String RegisterUserInDB() {
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			Connection con = DriverManager.getConnection("jdbc:db2://5.10.125.192:50000/SQLDB", "user03239",
					"1MDtRJ9K2I72");

			// Connection c = myDataSource.getConnection();
			PreparedStatement s = con.prepareStatement("INSERT INTO Users(name, email, password) VALUES(?,?,?)");

			s.setString(1, name);
			s.setString(2, password);
			s.setString(3, password);
			s.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			return ACTION_REGISTER_FAIL;
		}
		return ACTION_REGISTER_SUCCESS;
	}

}
