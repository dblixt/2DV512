package dv512.model.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.javadocmd.simplelatlng.LatLng;

import dv512.controller.util.DbManager;
import dv512.model.Event;
import dv512.model.Profile;

@Named
@ApplicationScoped
public class EventsDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private DbManager dbManager;

	public Event get(int eventId) {		
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT * FROM Events LEFT JOIN Profiles ON Events.user_id = Profiles.user_id WHERE id = ?");
			stmt.setInt(1, eventId);

			ResultSet r = stmt.executeQuery();

			Event event = new Event();
			if (r.next()) {
				
				Profile profile = new Profile();		
				profile.setUserId(r.getInt("user_id"));
				profile.setName(r.getString("name"));
				profile.setGender(r.getString("gender"));
				profile.setDescription(r.getString("description"));
				profile.setLatitude(r.getDouble("pos_lat"));
				profile.setLongitude(r.getDouble("pos_lng"));
				profile.setImage(r.getString("img"));
				
				event.setId(r.getInt("id"));
				event.setCreator(profile);
				event.setDate(r.getLong("utc_date"));
				event.setTitle(r.getString("title"));
				event.setDescription(r.getString("description"));
				event.setLongitude(r.getDouble("pos_lng"));
				event.setLatitude(r.getDouble("pos_lat"));
			}
			return event;
		} 
		catch (SQLException e) {
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
			stmt = con.prepareStatement(
					"INSERT INTO Events(user_id, utc_date, title, description, pos_lng, pos_lat, utc_date_modified) VALUES(?,?,?,?,?,?,?)");
			stmt.setInt(1, event.getCreator().getUserId());
			stmt.setLong(2, event.getDate());
			stmt.setString(3, event.getTitle());
			stmt.setString(4, event.getDescription());
			stmt.setDouble(5, event.getLongitude());
			stmt.setDouble(6, event.getLatitude());
			stmt.setLong(7,  Instant.now().getEpochSecond());
			stmt.executeUpdate();
			return true;
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

	
	public boolean join(int userId, int eventId, long date) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement(
					"INSERT INTO EventJoins(user_id, event_id, utc_date) VALUES(?,?,?)");
			stmt.setInt(1, userId);
			stmt.setInt(2, eventId);
			stmt.setLong(3, date);
			stmt.executeUpdate();
			return true;
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
	
	public boolean leave(int userId, int eventId) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("DELETE FROM EventJoins WHERE user_id = ? AND event_id = ?");
			stmt.setInt(1, userId);
			stmt.setInt(2, eventId);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}

		return false;
	}

	
	public List<Event> feed(int userId, LatLng origin, double radius) {
		List<Event> feed = new ArrayList<>();
			
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = createFeedStatement(con, userId, origin, radius);
			
			ResultSet r = stmt.executeQuery();
			while(r.next()) {
				Event e = new Event();
				e.setId(r.getInt("id"));
				e.getCreator().setUserId(r.getInt("user_id"));
				e.getCreator().setName(r.getString("creator_name"));
				e.getCreator().setImage(r.getString("creator_img"));
				e.setDate(r.getLong("utc_date"));
				e.setTitle(r.getString("title"));
				e.setDescription(r.getString("description"));
				e.setLatitude(r.getDouble("pos_lat"));
				e.setLongitude(r.getDouble("pos_lng"));
				
				int joined = r.getInt("joined");
				int approved = r.getInt("join_approved");				
				if(joined > 0) {
					if(approved > 0) {
						e.setJoinStatus(Event.JOIN_STATUS_JOINED);
					}
					else {
						e.setJoinStatus(Event.JOIN_STATUS_JOIN_REQUESTED);
					}
				}
				else {
					e.setJoinStatus(Event.JOIN_STATUS_UNJOINED);
				}

				e.setDistance(r.getDouble("distance"));		
				feed.add(e);
			}		
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}

		return feed;		
	}
		
	private PreparedStatement createFeedStatement(Connection con, 
			int userId, LatLng origin, double radius) throws SQLException {
//		-------------------------------------------
// 		Efficiency: not sure, given indices on 
//		-------------------------------------------
//		Events.user_id
//		Events.pos_lat
//		Events.pos_lng
//		Events.utc_date
//		Events.utc_date_modified
//		EventJoins.user_id
//		EventJoins.event_id
//		-------------------------------------------
//		it might be ok.
	
		String sql = 
				"SELECT s.id, s.user_id, u.name AS creator_name, u.img AS creator_img, s.utc_date, " + 
				"s.title, s.description, s.pos_lat, s.pos_lng, j.user_id AS joined, " +
				"j.approved AS join_approved, distance, s.utc_date_modified, radius FROM ( " + 
				"	SELECT e.id, e.user_id, title, description, " + 
				"	utc_date, e.pos_lat, e.pos_lng, e.utc_date_modified, p.radius, " + 
				"	p.distance_unit " + 
				"		* DEGREES(ACOS(COS(RADIANS(p.latpoint)) " + 
				"		* COS(RADIANS(e.pos_lat)) " + 
				"		* COS(RADIANS(p.longpoint - e.pos_lng)) " + 
				"		+ SIN(RADIANS(p.latpoint)) " + 
				"		* SIN(RADIANS(e.pos_lat)))) AS distance " + 
				"	FROM Events AS e " + 
				"	JOIN ( " + 
				"		SELECT " + origin.getLatitude() + " AS latpoint, " + 
						origin.getLongitude() + " AS longpoint, " + 
						radius + " AS radius, 111.045 AS distance_unit " + 
				"		FROM sysibm.sysdummy1 " + 
				"	) AS p ON 1=1 " + 
				"	WHERE e.pos_lat " + 
				"		BETWEEN p.latpoint  - (p.radius / p.distance_unit) " + 
				"		AND p.latpoint + (p.radius / p.distance_unit) " + 
				"	AND e.pos_lng " + 
				"		BETWEEN p.longpoint - (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint)))) " + 
				"		AND p.longpoint + (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint)))) " + 
				") AS s " + 
				"LEFT JOIN Profiles AS u ON u.user_id = s.user_id " + 
				"LEFT JOIN EventJoins AS j ON j.event_id = s.id AND j.user_id = ? " +
				"WHERE distance <= radius " +
				"ORDER BY utc_date_modified DESC " + 
				"FETCH FIRST 100 ROWS ONLY";
				
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, userId);
		return stmt;
	}
	
}
