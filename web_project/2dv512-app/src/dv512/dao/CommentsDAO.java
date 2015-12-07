package dv512.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dv512.controller.util.DbManager;
import dv512.model.Comment;

public class CommentsDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2362157639058508206L;

	@Inject
	private DbManager dbManager;

	public boolean insert(Comment comment) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement(
					"INSERT INTO Events(event_id, user_id, date, title, description, pos_lng, pos_lat) VALUES(?,?,?,?,?,?,?)");
			stmt.setInt(1, comment.getCommentId());
			stmt.setInt(2, comment.getEventId());
			stmt.setInt(3, comment.getUserID());
			stmt.setLong(4, comment.getDateTime());
			stmt.setString(5, comment.getComment());
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

	public List<Comment> listAll(int eventID) {
		List<Comment> commentList = new ArrayList<>();

		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT * FROM Comments WHERE event_id = ?");
			stmt.setInt(1, eventID);

			ResultSet r = stmt.executeQuery();

			while (r.next()) {

				Comment comment = new Comment();
				comment.setCommentId(r.getInt("id"));
				comment.setEventId(r.getInt("event_id"));
				comment.setUserID(r.getInt("user_id"));
				comment.setDateTime(r.getLong("date"));
				comment.setComment(r.getString("comment"));

				commentList.add(comment);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}

		return commentList;
	}

}
