package dv512.model.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.javadocmd.simplelatlng.LatLng;

import dv512.controller.util.DbManager;
import dv512.model.Dog;
import dv512.model.Event;
import dv512.model.FeedEvent;
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


	public List<FeedEvent> feed(int userId, LatLng origin, double radius) {
		List<FeedEvent> feed = new ArrayList<>();
			
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = createFeedStatement(con, userId, origin, radius);
			
			ResultSet r = stmt.executeQuery();
			while(r.next()) {
				FeedEvent e = new FeedEvent();
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
			
			if(!feed.isEmpty()) {
				fetchDogs(con, feed);
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
	
	
	
	private void fetchDogs(Connection con, List<FeedEvent> events) {
//		SELECT event_id, LISTAGG(id, ',') AS dog_ids, LISTAGG(name, ',') AS dog_names, LISTAGG(img, ',') AS dog_imgs FROM (
//			SELECT e.id AS event_id,d.id,d.name,d.img FROM Events AS e
//			JOIN Dogs AS d ON d.user_id = e.user_id
//			WHERE e.id IN(1,2)
//			UNION 
//			SELECT e.event_id AS event_id,d.id,d.name, d.img 
//			FROM EventJoins AS e JOIN Dogs AS d ON e.user_id = d.user_id
//			WHERE e.event_id IN(1,2)
//		) GROUP BY event_id
		
		HashMap<Integer, FeedEvent> map = new HashMap<>();
		
		StringBuilder ids = new StringBuilder();
		for(int i = 0; i < events.size(); i++) {
			FeedEvent e = events.get(i);			
			map.put(e.getId(), e);
			
			ids.append(e.getId());
			if(i < events.size() - 1) {
				ids.append(',');
			}			
		}
					
		String sql = 
				"SELECT event_id, LISTAGG(img, ',') AS dog_imgs FROM ( " + 
				"	SELECT e.id AS event_id,d.img FROM Events AS e " + 
				"	JOIN Dogs AS d ON d.user_id = e.user_id " + 
				"	WHERE e.id IN(" + ids + ") " + 
				"	UNION " + 
				"	SELECT e.event_id AS event_id,d.img " + 
				"	FROM EventJoins AS e JOIN Dogs AS d ON e.user_id = d.user_id " + 
				"	WHERE e.event_id IN(" + ids + ") " + 
				") GROUP BY event_id";
		

		Statement stmt = null;
		try {
			stmt = con.createStatement();

			ResultSet r = stmt.executeQuery(sql);
			while(r.next()) {
				int id = r.getInt("event_id");
				
				FeedEvent e = map.get(id);				
				if(e != null) {
					String dogImages[] = r.getString("dog_imgs").split(",");
					
					int len = dogImages.length;
					for(int i = 0; i < (len > 6 ? 6 : len); i++) {
						Dog d = new Dog();
						d.setImage(dogImages[i]);
						e.getDogs().add(d);
					}
				}
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			dbManager.close(stmt);
		}
	}
	
	private PreparedStatement createFeedStatement(Connection con, 
			int userId, LatLng origin, double radius) throws SQLException {
//		-------------------------------------------
//		Two part query for fetching feed.
// 		Efficiency: not sure, given index on 
//		-------------------------------------------
//		Events.user_id
//		Events.pos_lat
//		Events.pos_lng
//		Events.date
//		Dogs.user_id
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
