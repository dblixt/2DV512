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
import dv512.model.Event;

@Named
@SessionScoped
public class EventsDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5573703730766227781L;

	@Inject
	private DbManager dbManager;
		
	public Event get(int eventId) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT * FROM Events WHERE event_id = ?");
			stmt.setInt(1, eventId);
		
			ResultSet r = stmt.executeQuery();
			
			Event event = new Event();
			if(r != null && r.next()) {
				event.setId(r.getInt("event_id"));
				event.setUserId(r.getInt("user_id"));
				event.setDateTime(r.getLong("date"));
				event.setTitle(r.getString("title"));
				event.setDescription(r.getString("description"));
				event.setLongitude(r.getDouble("pos_lng"));
				event.setLatitude(r.getDouble("pos_lat"));		
			}		
			
			return event;
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
	
	public boolean insert(Event event) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("INSERT INTO Events(event_id, user_id, date, title, description, pos_lng, pos_lat) VALUES(?,?,?,?,?,?,?)");
			stmt.setInt(1, event.getId());
			stmt.setInt(2, event.getUserId());
			stmt.setLong(3, event.getDateTime());
			stmt.setString(4, event.getTitle());
			stmt.setString(5, event.getDescription());
			stmt.setDouble(6, event.getLongitude());
			stmt.setDouble(7, event.getLatitude());			
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

	public void update(Event event) {
		// TODO Auto-generated method stub
		
	}
	
	
//	public boolean update(Event profile) {
//		Connection con = null;
//		PreparedStatement stmt = null;
//		try {
//			con = dbManager.getConnection();
//			stmt = con.prepareStatement("UPDATE Profiles SET name = ?, gender = ?, description = ?, img = ?, pos_lng = ?, pos_lat = ? WHERE user_id = ?");
//			stmt.setString(1, profile.getName());
//			stmt.setString(2, profile.getGender());
//			stmt.setString(3, profile.getDescription());
//			stmt.setString(4, profile.getProfilePic());
//			stmt.setDouble(5, profile.getLongitude());
//			stmt.setDouble(6, profile.getLatitude());
//			stmt.setInt(7, profile.getUserId());
//							
//			stmt.executeUpdate();	
//			return true;
//		}
//		catch(SQLException e) {
//			e.printStackTrace();			
//		}
//		finally {
//			dbManager.close(stmt);
//			dbManager.close(con);
//		}		
//		
//		return false;
//	}
	
}
