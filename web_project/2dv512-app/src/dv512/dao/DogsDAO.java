package dv512.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.DbManager;
import dv512.model.Dog;

@Named
@SessionScoped
public class DogsDAO implements Serializable {

	private static final long serialVersionUID = -5830076680423829149L;
	
	@Inject
	private DbManager dbManager;
	
	public Dog get(int id) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT * FROM Dogs WHERE id = ?");
			stmt.setInt(1, id);
			
			ResultSet r = stmt.executeQuery();
			if(r.next()) {
				Dog d = new Dog();
				d.setId(r.getInt("id"));
				d.setUserId(r.getInt("user_id"));
				d.setName(r.getString("name"));
				d.setRace(r.getString("race"));
				d.setAge(r.getInt("age"));
				d.setGender(r.getString("gender"));
				d.setPicture(r.getString("img"));
				return d;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}	
		
		return null;
	}
	
	public List<Dog> listAll(int userId) {
		List<Dog> dl = new ArrayList<>();
		
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("SELECT * FROM Dogs WHERE user_id = ?");
			stmt.setInt(1, userId);
			
			ResultSet r = stmt.executeQuery();
			
			
			while(r.next()) {
				Dog d = new Dog();
				d.setId(r.getInt("id"));
				d.setUserId(r.getInt("user_id"));
				d.setName(r.getString("name"));
				d.setRace(r.getString("race"));
				d.setAge(r.getInt("age"));
				d.setGender(r.getString("gender"));
				d.setPicture(r.getString("img"));
				
				dl.add(d);
			}	
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}	
		
		return dl;
	}
	
	
	public boolean insert(Dog dog) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("INSERT INTO Dogs(user_id,name,race,age,gender,img) VALUES(?,?,?,?,?,?)");
			stmt.setInt(1, dog.getUserId());
			stmt.setString(2, dog.getName());
			stmt.setString(3, dog.getRace());
			stmt.setInt(4, dog.getAge());
			stmt.setString(5, dog.getGender());
			stmt.setString(6, dog.getPicture());
			
									
			stmt.executeUpdate();		
			return true;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}			
				
		return false;		
	}

	public boolean update(Dog dog) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();		
			stmt = con.prepareStatement("UPDATE Dogs SET name = ?, gender = ?, race = ?, age = ?, img = ? WHERE id = ?");
			stmt.setString(1, dog.getName());
			stmt.setString(2, dog.getGender());
			stmt.setString(3, dog.getRace());
			stmt.setInt(4, dog.getAge());
			stmt.setString(5, dog.getPicture());
			stmt.setInt(6, dog.getId());
								
			stmt.executeUpdate();		
			return true;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}			
		
		return false;
	}
	
	public boolean delete(int id) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = dbManager.getConnection();
			stmt = con.prepareStatement("DELETE FROM Dogs WHERE id = ?");
			stmt.setInt(1, id);
			
			stmt.executeUpdate();	
			return true;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			dbManager.close(stmt);
			dbManager.close(con);
		}
		
		return false;
	}
	
}
