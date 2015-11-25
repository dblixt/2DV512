package dv512.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.sql.DataSource;

public class DbManager {

	private static final boolean localTesting = false;

	@Resource(lookup = "jdbc/app-db")
	private static DataSource dataSource;
	
	
	public static Connection getConnection() throws SQLException {
		
		if(localTesting) {
			try {
				Class.forName("com.ibm.db2.jcc.DB2Driver");
			}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			return DriverManager.getConnection("jdbc:db2://5.10.125.192:50000/SQLDB", "user03239", "1MDtRJ9K2I72");
		}
		else if(dataSource != null) {
			return dataSource.getConnection();
		}		
		
		return null;
	}
	
	public static void close(Connection con) {
		if(con == null) 
			return;
		
		try {
			con.close();
		}
		catch(Exception e) {};
	}
	
	public static void close(Statement stmt) {
		if(stmt == null)
			return;
		
		try {
			stmt.close();
		}
		catch(Exception e) {}
	}
	

	
}
