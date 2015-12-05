package dv512.controller.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.sql.DataSource;


@Stateless
@Named
public class DbManager {

	// configured to be the same for both local testing and bluemix server.
	@Resource(lookup = "jdbc/app-db")
	private DataSource dataSource;
	

	public Connection getConnection() throws SQLException {
		if(dataSource != null) {
			return dataSource.getConnection();
		}		
		
		return null;
	}
	
	public void close(Connection con) {
		if(con == null) 
			return;
		
		try {
			con.close();
		}
		catch(Exception e) {};
	}
	
	public void close(Statement stmt) {
		if(stmt == null)
			return;
		
		try {
			stmt.close();
		}
		catch(Exception e) {}
	}
		
}
