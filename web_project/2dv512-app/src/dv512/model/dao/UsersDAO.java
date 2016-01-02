package dv512.model.dao;

import java.io.ByteArrayInputStream;
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
import dv512.controller.util.PasswordUtils;
import dv512.model.User;

@Named
@ApplicationScoped
public class UsersDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	
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

			s = con.prepareStatement("INSERT INTO Users(email, password, salt) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			byte[] salt = PasswordUtils.getNextSalt();
			byte[] hashedPw = PasswordUtils.hash(user.getPassword().toCharArray(), salt);
			
			s.setString(1, user.getEmail());
			s.setBlob(2, new ByteArrayInputStream(hashedPw), hashedPw.length);
			s.setBlob(3, new ByteArrayInputStream(salt), salt.length);
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
			stmt = con.prepareStatement("SELECT * FROM Users WHERE email = ?");
			stmt.setString(1, user.getEmail());
	
			ResultSet r = stmt.executeQuery();
			if (r != null && r.next()) {
				byte[] salt = r.getBytes("salt");
				byte[] hashedPw = r.getBytes("password");
				
				boolean ok = PasswordUtils.check(
						user.getPassword().toCharArray(), 
						salt, hashedPw);
				
				if(ok) {
					user.setId(r.getInt("id"));				
					user.setPassword(null); // no need to store.
					return true;
				}
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
	
	
	/**
	 * Create a reset_token for the user of the given 
	 * email. If no such user exists null will be returned.
	 * @param user email address of the user
	 * @return the created token if user exists, null otherwise.
	 */
	public String requestResetPassword(User user) {		
		String token = PasswordUtils.getToken();
			
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("UPDATE Users SET reset_token = ? WHERE email = ?", Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, token);
			stmt.setString(2, user.getEmail());

			int affected = stmt.executeUpdate();
			
			if(affected == 1) {
				return token;
			}			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(con);
			dbManager.close(stmt);
		}
		
		return null;
	}
	
	public boolean updatePassword(String resetToken, String password) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("UPDATE Users SET reset_token = NULL, password = ?, salt = ? WHERE reset_token = ?", Statement.RETURN_GENERATED_KEYS);
			
			byte[] salt = PasswordUtils.getNextSalt();
			byte[] hashedPw = PasswordUtils.hash(password.toCharArray(), salt);
			
			stmt.setBlob(1, new ByteArrayInputStream(hashedPw), hashedPw.length);
			stmt.setBlob(2, new ByteArrayInputStream(salt), salt.length);
			stmt.setString(3, resetToken);
	
			int affected = stmt.executeUpdate();			
			if(affected == 1) {
				return true;
			}			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(con);
			dbManager.close(stmt);
		}
		
		return false;
	}
	
}
