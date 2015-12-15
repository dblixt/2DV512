package dv512.model.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
<<<<<<< HEAD

=======
		
>>>>>>> refs/remotes/origin/dbPostNoSqlSwitch
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
<<<<<<< HEAD
			stmt = con.prepareStatement("SELECT * FROM Events WHERE id = ?");
=======
			stmt = con.prepareStatement("SELECT * FROM Events LEFT JOIN Profiles ON Events.user_id = Profiles.user_id WHERE id = ?");
>>>>>>> refs/remotes/origin/dbPostNoSqlSwitch
			stmt.setInt(1, eventId);

			ResultSet r = stmt.executeQuery();

			Event event = new Event();
			if (r != null && r.next()) {
				event.setId(r.getInt("id"));
<<<<<<< HEAD
				event.setId(r.getInt("user_id"));
=======
				Profile p = new Profile();
				p.setUserId(r.getInt("user_id"));
				event.setCreator(p);
>>>>>>> refs/remotes/origin/dbPostNoSqlSwitch
				event.setDate(r.getLong("date"));
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
<<<<<<< HEAD

=======
		
>>>>>>> refs/remotes/origin/dbPostNoSqlSwitch
		return null;

		/*
		 * Connection con = null; PreparedStatement stmt = null; try { con =
		 * dbManager.getConnection(); stmt = con.prepareStatement(
		 * "SELECT * FROM Events LEFT JOIN Profiles ON Events.user_id WHERE id = ?"
		 * ); stmt.setInt(1, eventId);
		 * 
		 * ResultSet r = stmt.executeQuery();
		 * 
		 * Event event = new Event(); if (r != null && r.next()) {
		 * event.setId(r.getInt("id")); event.setUserId(r.getInt("user_id"));
		 * event.setDateTime(r.getLong("date"));
		 * event.setTitle(r.getString("title"));
		 * event.setDescription(r.getString("description"));
		 * event.setLongitude(r.getDouble("pos_lng"));
		 * event.setLatitude(r.getDouble("pos_lat")); }
		 * 
		 * return event; } catch (SQLException e) { e.printStackTrace(); }
		 * finally { dbManager.close(stmt); dbManager.close(con); }
		 */

	}

	public boolean insert(Event event) {
<<<<<<< HEAD

=======
		
>>>>>>> refs/remotes/origin/dbPostNoSqlSwitch
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement(
					"INSERT INTO Events(user_id, date, title, description, pos_lng, pos_lat) VALUES(?,?,?,?,?,?)");
<<<<<<< HEAD
			stmt.setInt(1, event.getId());
=======
			stmt.setInt(1, event.getCreator().getUserId());
>>>>>>> refs/remotes/origin/dbPostNoSqlSwitch
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
<<<<<<< HEAD

=======
		
>>>>>>> refs/remotes/origin/dbPostNoSqlSwitch
		return false;
	}

	public void createTestEvent() {

		Event event = new Event();

		event.setId(1);
		event.setDate(System.currentTimeMillis());
		event.setTitle("Wark in the park");
		event.setDescription(
				"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est.");
		event.setLatitude(56.8483223);
		event.setLatitude(14.8193872);
		insert(event);

		eventJoin(1, 1, System.currentTimeMillis());

	}

	public boolean eventJoin(int userId, int eventId, long date) {

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("INSERT INTO EventJoins(user_id, event_id, date) VALUES(?,?,?)");
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

}
