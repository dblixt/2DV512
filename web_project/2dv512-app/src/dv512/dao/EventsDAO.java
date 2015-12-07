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
import dv512.model.Comment;
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
			stmt = con.prepareStatement("SELECT * FROM Events WHERE id = ?");
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

		return null;
	}

	public boolean insert(Event event) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement(
					"INSERT INTO Events(user_id, date, title, description, pos_lng, pos_lat) VALUES(?,?,?,?,?,?)");
			stmt.setInt(1, event.getUserId());
			stmt.setLong(2, event.getDateTime());
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

	public void createTestEvent() {

		Event event = new Event();

		event.setUserId(1);
		event.setDateTime(System.currentTimeMillis());
		event.setTitle("Wark in the park");
		event.setDescription(
				"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est.");
		event.setLatitude(56.8483223);
		event.setLatitude(14.8193872);
		insert(event);

	}

}
