package dv512.model.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;

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
		Connection con = null;
		PreparedStatement s = null;

		try {
			con = dbManager.getConnection();

			s = con.prepareStatement(
					"INSERT INTO Notifications(type, receiver_user_id, " + 
					"trigger_user_id, event_id, comment_id, utc_date) VALUES(?,?,?,?,?,?)");

			s.setInt(1, n.getType());
			s.setInt(2, n.getReceiverUserId());
			s.setInt(3, n.getTriggerUserId());
			s.setInt(4, n.getEventId());
			s.setInt(5, n.getCommentId());
			s.setLong(6, Instant.now().getEpochSecond());
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
