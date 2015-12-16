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
			if (r != null && r.next()) {
				
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
				event.setDate(r.getLong("date"));
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
					"INSERT INTO Events(user_id, date, title, description, pos_lng, pos_lat) VALUES(?,?,?,?,?,?)");
			stmt.setInt(1, event.getCreator().getUserId());
			stmt.setLong(2, event.getDate());
			stmt.setString(3, event.getTitle());
			stmt.setString(4, event.getDescription());
			stmt.setDouble(5, event.getLongitude());
			stmt.setDouble(6, event.getLatitude());
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

	
	public boolean eventJoin(int userId, int eventId, long date) {

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement(
					"INSERT INTO EventJoins(user_id, event_id, date) VALUES(?,?,?)");
			stmt.setInt(1, userId);
			stmt.setInt(2, eventId);
			stmt.setLong(3, date);
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
				e.getCreator().setId(r.getInt("user_id"));
				e.getCreator().setName(r.getString("creator_name"));
				e.getCreator().setImage(r.getString("creator_img"));
				e.setDate(r.getLong("date"));
				e.setTitle(r.getString("title"));
				e.setDescription(r.getString("description"));
				e.setLatitude(r.getDouble("pos_lat"));
				e.setLongitude(r.getDouble("pos_lng"));
				e.setJoined(r.getInt("joined") > 0);
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
// 		Efficiency: not sure, given index on 
//		-------------------------------------------
//		Events.user_id
//		Events.pos_lat
//		Events.pos_lng
//		Events.date
//		EventJoins.user_id
//		EventJoins.event_id
//		-------------------------------------------
//		I don't think it would be too bad since the first selection
//		is limited to 100 rows.
//		
//		
//		SELECT s.id, s.user_id, u.name AS creator_name, u.img AS creator_img, s.date, 
//		s.title, s.description, s.pos_lat, s.pos_lng, j.user_id AS joined, distance, radius FROM (
//			SELECT e.id, e.user_id, title, description, 
//			date, e.pos_lat, e.pos_lng, p.radius, 
//			p.distance_unit 
//				* DEGREES(ACOS(COS(RADIANS(p.latpoint)) 
//				* COS(RADIANS(e.pos_lat)) 
//				* COS(RADIANS(p.longpoint - e.pos_lng)) 
//				+ SIN(RADIANS(p.latpoint)) 
//				* SIN(RADIANS(e.pos_lat)))) AS distance 
//			FROM Events AS e 
//			JOIN (
//				SELECT 56.8 AS latpoint, 14.8 AS longpoint, 
//				50.0 AS radius, 111.045 AS distance_unit 
//				FROM sysibm.sysdummy1 
//			) AS p ON 1=1 
//			WHERE e.pos_lat 
//				BETWEEN p.latpoint  - (p.radius / p.distance_unit) 
//				AND p.latpoint + (p.radius / p.distance_unit) 
//			AND e.pos_lng 
//				BETWEEN p.longpoint - (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint)))) 
//				AND p.longpoint + (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint)))) 
//			ORDER BY date
//			FETCH FIRST 100 ROWS ONLY
//		) AS s 
//		LEFT JOIN Profiles AS u ON u.user_id = s.user_id 
//		LEFT JOIN EventJoins AS j ON j.event_id = s.id AND j.user_id = userId 
//		WHERE distance <= radius
//
	
		String sql = 
				"SELECT s.id, s.user_id, u.name AS creator_name, u.img AS creator_img, s.date, " + 
				"s.title, s.description, s.pos_lat, s.pos_lng, j.user_id AS joined, distance, radius FROM ( " + 
				"	SELECT e.id, e.user_id, title, description, " + 
				"	date, e.pos_lat, e.pos_lng, p.radius, " + 
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
				"	ORDER BY date " + 
				"	FETCH FIRST 100 ROWS ONLY " + 
				") AS s " + 
				"LEFT JOIN Profiles AS u ON u.user_id = s.user_id " + 
				"LEFT JOIN EventJoins AS j ON j.event_id = s.id AND j.user_id = ? " +
				"WHERE distance <= radius";
				
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setInt(1, userId);
		return stmt;
	}
	
}
