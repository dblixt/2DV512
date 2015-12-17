package dv512.controller.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;


@ApplicationScoped
@Named
public class DbManager {

	// configured to be the same for both local testing and bluemix server.
	@Resource(lookup = "jdbc/app-db")
	private DataSource dataSource;
	
	@Resource(name = "couchdb/nosql-app-db")
	private CouchDbInstance db;	
	private CouchDbConnector dbc;
	
	
	public CouchDbConnector getImgConnection() {
		if(dbc == null) {
			dbc = db.createConnector("app-img-db", true);
		}
		
		return dbc;
	}

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
