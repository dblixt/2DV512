package dv512.controller.util;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;

@Named
@ApplicationScoped
public class NosqlManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final String DATABASE_NAME = "app-db";
	private static final String DATABASE_NAME_IMG ="app-img-db";
	
	
	@Resource(name = "couchdb/nosql-app-db")
	private CouchDbInstance db;
	
	private CouchDbConnector dbc;
	private CouchDbConnector dbcImg;
	

	public CouchDbConnector getConnection() {
		if(dbc == null) {
			dbc = db.createConnector(DATABASE_NAME, true);		
		}
		
		return dbc;
	}
	
	public CouchDbConnector getImgConnection() {
		if(dbcImg == null) {
			dbcImg = db.createConnector(DATABASE_NAME_IMG, true);		
		}
		
		return dbcImg;
	}
	
}
