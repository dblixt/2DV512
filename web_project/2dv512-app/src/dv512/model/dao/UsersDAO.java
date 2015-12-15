package dv512.model.dao;

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
public class UsersDAO implements Serializable {

	private static final long serialVersionUID = -8679482635026754077L;
	
	@Inject
	private DbManager dbManager;

	
	/**
	 * <p>Insert a new user into Users table.</p>
	 * 
	 * <p>The auto generated id will be set on
	 * <code>user</code> if the insert is successful.</p>
	 * 
	 * @param user
	 * @return true if insert was successful, false otherwise.
	 */
	public boolean insert(User user) {
		Connection con = null;
		PreparedStatement s = null;

		try {
			con = dbManager.getConnection();

			s = con.prepareStatement("INSERT INTO Users(email, password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);

			s.setString(1, user.getEmail());
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

	/**
	 * Check if the combination of email and password
	 * is valid.
	 * 
	 * Id will be set on user to the real database 
	 * id if the combination is valid.
	 * 
	 * @param user
	 * @return true if user is valid, false otherwise.
	 */
	public boolean verify(User user) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT id FROM Users WHERE email = ? AND password = ?");
			stmt.setString(1, user.getEmail());
			stmt.setString(2, user.getPassword());

			ResultSet r = stmt.executeQuery();
			if (r != null && r.next()) {
				System.out.println("User verification succeded!");
				user.setId(r.getInt("id"));				
				user.setPassword(null); // no need to store.
				return true;
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}

		return false;
	}
	

}
