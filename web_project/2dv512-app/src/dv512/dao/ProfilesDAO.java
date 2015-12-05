package dv512.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.DbManager;
import dv512.model.Profile;

@Named
@SessionScoped
public class ProfilesDAO implements Serializable {

	private static final long serialVersionUID = 5568131005011243224L;
	
	@Inject
	private DbManager dbManager;
	
	
	public Profile get(int userId) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT * FROM Profiles WHERE user_id = ?");
			stmt.setInt(1, userId);
		
			ResultSet r = stmt.executeQuery();
			
			Profile profile = new Profile();
			if(r != null && r.next()) {
				profile.setUserId(r.getInt("id"));
				profile.setName(r.getString("name"));
				profile.setGender(r.getString("gender"));
				profile.setDescription(r.getString("description"));
				profile.setLatitude(r.getDouble("pos_lat"));
				profile.setLongitude(r.getDouble("pos_lng"));
				profile.setProfilePic(r.getString("img"));	
			}		
			
			return profile;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}	
		
		return null;
	}
	
	public boolean update(Profile profile) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("UPDATE Profiles SET name = ?, gender = ?, description = ?, img = ?, pos_lng = ?, pos_lat = ? WHERE user_id = ?");
			stmt.setString(1, profile.getName());
			stmt.setString(2, profile.getGender());
			stmt.setString(3, profile.getDescription());
			stmt.setString(4, profile.getProfilePic());
			stmt.setDouble(5, profile.getLongitude());
			stmt.setDouble(6, profile.getLatitude());
			stmt.setInt(7, profile.getUserId());
							
			stmt.executeUpdate();	
			return true;
		}
		catch(SQLException e) {
			e.printStackTrace();			
		}
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}		
		
		return false;
	}
	
}
