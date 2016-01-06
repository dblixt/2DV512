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

import dv512.controller.util.DbManager;
import dv512.model.Comment;
import dv512.model.Profile;

@Named
@ApplicationScoped
public class CommentsDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private DbManager dbManager;

	public boolean insert(Comment comment) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement(
					"INSERT INTO Comments(event_id,user_id, date, comment) VALUES(?,?,?,?)");
			stmt.setInt(1, comment.getEventId());
			stmt.setInt(2, comment.getUserId());
			stmt.setLong(3, comment.getDate());
			stmt.setString(4, comment.getComment());
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
			stmt = con.prepareStatement("SELECT * FROM Comments LEFT JOIN Profiles ON Comments.user_id = Profiles.user_id WHERE Comments.event_id = ? ORDER BY Comments.date ASC");
			stmt.setInt(1, eventID);

			ResultSet r = stmt.executeQuery();

			while (r.next()) {
				
				Profile profile = new Profile();
				profile.setUserId(r.getInt("user_id"));
				profile.setName(r.getString("name"));
				profile.setGender(r.getString("gender"));
				profile.setDescription(r.getString("description"));
				profile.setLatitude(r.getDouble("pos_lat"));
				profile.setLongitude(r.getDouble("pos_lng"));
				profile.setImage(r.getString("img"));
				
				Comment comment = new Comment();
				comment.setId(r.getInt("id"));
				comment.setEventId(r.getInt("event_id"));
				comment.setUserId(r.getInt("user_id"));
				comment.setDate(r.getLong("date"));
				comment.setComment(r.getString("comment"));
				comment.setProfile(profile);

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
	
	public void createTestComment(){
		
		Comment comment = new Comment();
		comment.setUserId(2);
		comment.setComment("Hello this is a test comment from user 2");
		comment.setEventId(1);
		comment.setDate(Instant.now().getEpochSecond());
		insert(comment);
	}

}
