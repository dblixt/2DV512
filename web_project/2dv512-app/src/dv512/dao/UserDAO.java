package dv512.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.DbManager;
import dv512.model.User;

@Named
@ApplicationScoped
public class UserDAO implements Serializable{

	private static final long serialVersionUID = -8679482635026754077L;
	
	@Inject
	private DbManager dbManager;
		
	public boolean insert(User user) {
		Connection con = null;
		PreparedStatement s = null;

		try {
			con = dbManager.getConnection();

			s = con.prepareStatement("INSERT INTO Users(email, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);

			s.setString(1, user.getName());
			s.setString(2, user.getPassword());
			s.executeUpdate();

			
			// retrieve auto generated user id.
			ResultSet key = s.getGeneratedKeys();
			if (key.next()) {
				user.setId(key.getInt("id"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			
		} finally {
			dbManager.close(con);
			dbManager.close(s);
		}
		return true;
	}
	
	
}
