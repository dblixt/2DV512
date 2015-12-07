package dv512.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.DbManager;
import dv512.model.User;

@Named
@SessionScoped
public class RegisterDAO implements Serializable{

	private static final long serialVersionUID = -8679482635026754077L;
	
	private final int SUCCESS_MODE = 2;
	private final int FAILED_MODE = 3;

	@Inject
	private DbManager dbManager;
	
	
	public int register(User user) {
		Connection con = null;
		PreparedStatement s = null;

		try {
			con = dbManager.getConnection();

			s = con.prepareStatement("INSERT INTO Users(email, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);

			s.setString(1, user.getName());
			s.setString(2, user.getPassword());
			s.executeUpdate();

			int userId = 0;
			// retrieve auto generated user id.
			ResultSet key = s.getGeneratedKeys();
			if (key.next()) {
				userId = key.getInt("id");
			}
			
			dbManager.close(s);

			s = con.prepareStatement("INSERT INTO Profiles(user_id,name) VALUES(?,?)");
			s.setInt(1, userId);
			s.setString(2, user.getName());

			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return FAILED_MODE;
			
		} finally {
			dbManager.close(con);
			dbManager.close(s);
		}
		return SUCCESS_MODE;
	}
	
	
}
