package dv512.model.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.DbManager;
import dv512.model.Notification;

@Named
@ApplicationScoped
public class NotificationsDAO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private DbManager dbManager;

	
	public boolean insert(Notification n) {
		List<Notification> l  = new ArrayList<>();
		l.add(n);
		return insert(l);
	}

	public boolean insert(List<Notification> notifications) {
		if(notifications.size() < 1) {
			return false;
		}
				
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = dbManager.getConnection();

			stmt = con.prepareStatement(
					"INSERT INTO Notifications(type, target_user_id, " + 
					"source_user_id, event_id, utc_date) VALUES(?,?,?,?,?)");

			for(Notification n : notifications) {
				stmt.setInt(1, n.getType());
				stmt.setInt(2, n.getTargetUser().getUserId());
				stmt.setInt(3, n.getSourceUser().getUserId());
				stmt.setInt(4, n.getEvent().getId());
				stmt.setLong(5, n.getDate());
				stmt.addBatch();
			}
					
			stmt.executeBatch();
			return true;
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
	
	public List<Notification> listAll(int userId) {
		List<Notification> nl = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			
			stmt = con.prepareStatement(
					"SELECT n.id, n.type, n.utc_date, n.target_user_id, " + 
					"	pt.name AS target_user_name, pt.img AS target_user_img, " + 
					"	n.source_user_id, " + 
					"	ps.name AS source_user_name, " + 
					"	ps.img AS source_user_img, " + 
					"	n.event_id, e.title AS event_title " + 
					"FROM Notifications AS n " + 
					"JOIN Profiles AS pt ON pt.user_id = n.target_user_id " + 
					"JOIN Profiles AS ps ON ps.user_id = n.source_user_id " + 
					"JOIN Events AS e ON e.id = n.event_id " + 
					"WHERE target_user_id = ? " + 
					"ORDER BY utc_date DESC");
	
			stmt.setInt(1, userId);
			
			ResultSet r = stmt.executeQuery();
			while(r.next()) {
				Notification n = new Notification();
				n.setId(r.getInt("id"));
				n.setType(r.getInt("type"));
				n.setDate(r.getLong("utc_date"));
				n.getTargetUser().setUserId(r.getInt("target_user_id"));
				n.getTargetUser().setName(r.getString("target_user_name"));
				n.getTargetUser().setImage(r.getString("target_user_img"));
				n.getSourceUser().setUserId(r.getInt("source_user_id"));
				n.getSourceUser().setName(r.getString("source_user_name"));
				n.getSourceUser().setImage(r.getString("source_user_img"));
				n.getEvent().setId(r.getInt("event_id"));
				n.getEvent().setTitle(r.getString("event_title"));
				
				nl.add(n);
			}			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(con);
			dbManager.close(stmt);
		}
				
		return nl;
	}

	public int count(int userId) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT COUNT(*) AS count FROM Notifications WHERE target_user_id = ?");
			stmt.setInt(1, userId);
			ResultSet r = stmt.executeQuery();
			if(r.next()) {
				return r.getInt("count");
			}			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			dbManager.close(con);
			dbManager.close(stmt);
		}
		
		return 0;
	}
	
	public boolean remove(int id) {
		Connection con = null;
		PreparedStatement s = null;

		try {
			con = dbManager.getConnection();
			s = con.prepareStatement(
					"DELETE FROM Notifications WHERE id = ?");

			s.setInt(1, id);
			s.executeUpdate();
			return true;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			dbManager.close(con);
			dbManager.close(s);
		}
		
		return false;
	}
	
}
