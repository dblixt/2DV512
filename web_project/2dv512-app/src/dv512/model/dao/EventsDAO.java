package dv512.model.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

	/**
	 * Get information about a specific event.
	 * 
	 * @param eventId the id of the event to fetch.
	 * @param userId pass id of user to find out if the user has 
	 * 		  joined the event. Pass -1 if your don't care.
	 * @return
	 */
	public Event get(int eventId, int userId) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement(
					"SELECT e.id, e.user_id, e.utc_date, e.title, e.description, " +
					"   e.pos_lng, e.pos_lat, e.utc_date_modified, e.canceled, " + 
					"	p.name AS profile_name, p.gender AS profile_gender, " + 
					"   p.description AS profile_description, " + 
					"	p.pos_lng AS profile_pos_lng, p.pos_lat AS profile_pos_lat, " + 
					"   p.img AS profile_img, j.user_id AS joined, " + 
					"   j.approved AS join_approved " +
					"FROM Events AS e " + 
					"LEFT JOIN Profiles AS p ON e.user_id = p.user_id " + 
					"LEFT JOIN EventJoins AS j ON j.event_id = e.id AND j.user_id = ? " + 
					"WHERE e.id = ?"
			);
			stmt.setInt(1, userId);
			stmt.setInt(2, eventId);

			ResultSet r = stmt.executeQuery();
		
			if (r.next()) {
				Event event = new Event();

				Profile profile = new Profile();
				profile.setUserId(r.getInt("user_id"));
				profile.setName(r.getString("profile_name"));
				profile.setGender(r.getString("profile_gender"));
				profile.setDescription(r.getString("profile_description"));
				profile.setLatitude(r.getDouble("profile_pos_lat"));
				profile.setLongitude(r.getDouble("profile_pos_lng"));
				profile.setImage(r.getString("profile_img"));

				event.setId(r.getInt("id"));
				event.setCreator(profile);
				event.setDate(r.getLong("utc_date"));
				event.setTitle(r.getString("title"));
				event.setDescription(r.getString("description"));
				event.setLongitude(r.getDouble("pos_lng"));
				event.setLatitude(r.getDouble("pos_lat"));
				event.setCanceled(r.getInt("canceled") != 0);
							
				int joined = r.getInt("joined");
				int approved = r.getInt("join_approved");				
				if(joined > 0) {
					if(approved > 0) {
						event.setJoinStatus(Event.JOIN_STATUS_JOINED);
					}
					else {
						event.setJoinStatus(Event.JOIN_STATUS_JOIN_REQUESTED);
					}
				}
				else {
					event.setJoinStatus(Event.JOIN_STATUS_UNJOINED);
				}
				
				return event;
			}
			
			return null;
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
					"INSERT INTO Events(user_id, utc_date, title, description, pos_lng, pos_lat, utc_date_modified) VALUES(?,?,?,?,?,?,?)", 
					Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, event.getCreator().getUserId());
			stmt.setLong(2, event.getDate());
			stmt.setString(3, event.getTitle());
			stmt.setString(4, event.getDescription());
			stmt.setDouble(5, event.getLongitude());
			stmt.setDouble(6, event.getLatitude());
			stmt.setLong(7, Instant.now().getEpochSecond());
			
			if(stmt.executeUpdate() > 0) {
				ResultSet key = stmt.getGeneratedKeys();
				if(key.next()) {
					int id = key.getInt("id");
					join(event.getCreator().getUserId(), id, true);
				}	
				
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

	public boolean update(Event event) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement(
					"UPDATE Events SET user_id = ?, utc_date = ?, title = ?, description = ?, pos_lng = ?, pos_lat = ?, utc_date_modified = ? WHERE id = ?");
			stmt.setInt(1, event.getCreator().getUserId());
			stmt.setLong(2, event.getDate());
			stmt.setString(3, event.getTitle());
			stmt.setString(4, event.getDescription());
			stmt.setDouble(5, event.getLongitude());
			stmt.setDouble(6, event.getLatitude());
			stmt.setLong(7, Instant.now().getEpochSecond());
			stmt.setInt(8, event.getId());
						
			return stmt.executeUpdate() > 0;
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

	public boolean cancel(Event event) {
		Connection con = null;
		PreparedStatement stmt = null;

		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("UPDATE Events SET canceled = ? WHERE id = ?");
			stmt.setInt(1, 1);
			stmt.setInt(2, event.getId());
			
			return stmt.executeUpdate() > 0;
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
			System.out.println("Event Canceled");
		}

		return false;
	}
	
	
	public boolean join(int userId, int eventId, boolean approve) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("INSERT INTO EventJoins(user_id, event_id, utc_date, approved) VALUES(?,?,?,?)");
			stmt.setInt(1, userId);
			stmt.setInt(2, eventId);
			stmt.setLong(3, Instant.now().getEpochSecond());
			stmt.setInt(4, approve ? 1 : 0);
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

	public boolean approveJoin(int userId, int eventId) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("UPDATE EventJoins SET approved = 1 WHERE user_id = ? AND event_id = ?");

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
	
	/**
	 * Get list of users that have joined an event.
	 * @param eventId
	 * @return a list of profiles, but only userId will be set.
	 */
	public List<Profile> getJoins(int eventId) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT user_id FROM EventJoins WHERE event_id = ? AND approved = 1");
			stmt.setInt(1, eventId);
			ResultSet rs = stmt.executeQuery();
			List<Profile> userList = new ArrayList<>();
			
			while(rs.next()) {
				Profile p = new Profile();
				p.setUserId(rs.getInt("user_id"));
				userList.add(p);
			}
			
			return userList;
		} 
		catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
			System.out.println("Event Canceled");
		}

		return null;		
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

	private static PreparedStatement createFeedStatement(Connection con, 
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
				"	WHERE e.canceled = 0 " + 
				"   AND e.pos_lat " + 
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
