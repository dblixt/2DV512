package dv512.model.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.DbManager;
import dv512.model.Event;

@Named
@ApplicationScoped
public class EventsDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private DbManager dbManager;

	public Event get(int eventId) {
		/*
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT * FROM Events LEFT JOIN Profiles ON Events.user_id WHERE id = ?");
			stmt.setInt(1, eventId);

			ResultSet r = stmt.executeQuery();

			Event event = new Event();
			if (r != null && r.next()) {
				event.setId(r.getInt("id"));
				event.setUserId(r.getInt("user_id"));
				event.setDateTime(r.getLong("date"));
				event.setTitle(r.getString("title"));
				event.setDescription(r.getString("description"));
				event.setLongitude(r.getDouble("pos_lng"));
				event.setLatitude(r.getDouble("pos_lat"));
			}

			return event;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}
		*/
		return null;
	}

	public boolean insert(Event event) {		
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement(
					"INSERT INTO Events(user_id, date, title, description, pos_lng, pos_lat) VALUES(?,?,?,?,?,?)");
			stmt.setInt(1, event.getCreator().getId());
			stmt.setLong(2, event.getDate());
			stmt.setString(3, event.getTitle());
			stmt.setString(4, event.getDescription());
			stmt.setDouble(5, event.getLongitude());
			stmt.setDouble(6, event.getLatitude());
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

	public void createTestEvent() {
		/*
		Event event = new Event();

		event.setUserId(1);
		event.setDateTime(System.currentTimeMillis());
		event.setTitle("Wark in the park");
		event.setDescription(
				"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est.");
		event.setLatitude(56.8483223);
		event.setLatitude(14.8193872);
		insert(event);
		
		eventJoin(1,1,System.currentTimeMillis());
		*/
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

	
	
	public List<Event> feed() {
		
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = createFeedStatement(con);
			
			stmt.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}

		return null;		
	}
	
	private PreparedStatement createFeedStatement(Connection con) {
		//String sql = "SELECT * FROM ("
		
		
				
		
		
				
				/*
				
				
				SELECT zip, primary_city,
			       latitude, longitude, distance
			  FROM (
			 SELECT z.zip,
			        z.primary_city,
			        z.latitude, z.longitude,
			        p.radius,
			        p.distance_unit
			                 * DEGREES(ACOS(COS(RADIANS(p.latpoint))
			                 * COS(RADIANS(z.latitude))
			                 * COS(RADIANS(p.longpoint - z.longitude))
			                 + SIN(RADIANS(p.latpoint))
			                 * SIN(RADIANS(z.latitude)))) AS distance
			  FROM zip AS z
			  JOIN ( 
			        SELECT  42.81  AS latpoint,  -70.81 AS longpoint,
			                50.0 AS radius,      111.045 AS distance_unit
			    ) AS p ON 1=1
			  WHERE z.latitude
			     BETWEEN p.latpoint  - (p.radius / p.distance_unit)
			         AND p.latpoint  + (p.radius / p.distance_unit)
			    AND z.longitude
			     BETWEEN p.longpoint - (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint))))
			         AND p.longpoint + (p.radius / (p.distance_unit * COS(RADIANS(p.latpoint))))
			 ) AS d
			 WHERE distance <= radius
			 ORDER BY distance
			 LIMIT 15
		
			 */
		
		
		
		return null;
	}
	
	
}
