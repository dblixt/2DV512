package dv512.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.DbManager;

@Named
@SessionScoped
public class EditUserControler implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3988677729576317520L;

	public static final String ACTION_EDIT_SUCCESS = "success";
	public static final String ACTION_EDIT_FAIL = "fail";

	@Inject
	private DbManager dbManager;

	@Inject
	private LoginController loginController;

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

	public String edit() {
		Connection con = null;
		PreparedStatement s = null;

		try {
			con = dbManager.getConnection();

			// s = con.prepareStatement("UPDATE Users SET EMAIL = ? WHERE ID =
			// ?" ,
			// Statement.RETURN_GENERATED_KEYS);

			s = con.prepareStatement("UPDATE Users SET EMAIL = ?, PASSWORD = ?  WHERE ID = ?");

			s.setString(1, email);
			s.setString(2, password);
			s.setString(3, Integer.toString(loginController.getUserId()));
			s.executeUpdate();

			// int userId = 0;
			// // retrieve auto generated user id.
			// ResultSet key = s.getGeneratedKeys();
			// if(key.next()) {
			// userId = key.getInt("id");
			// }
			//
			// dbManager.close(s);
			//
			// s = con.prepareStatement("INSERT INTO Profiles(user_id,name)
			// VALUES(?,?)");
			// s.setInt(1, userId);
			// s.setString(2, name);
			//
			// s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return ACTION_EDIT_FAIL;
		} finally {
			dbManager.close(con);
			dbManager.close(s);
		}

		return ACTION_EDIT_SUCCESS;
	}

}
