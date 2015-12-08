package dv512.controller;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;

import dv512.model.nosql.User;

@Named
@RequestScoped
public class NosqlController {

	@Resource(name = "couchdb/nosql-app-db")
	protected CouchDbInstance db;

	public void test() {
		System.out.println("Testing nosql");

		
		
		
	}

	public User getUser() {
		CouchDbConnector dbc = db.createConnector("app-db", true);		
		return dbc.get(User.class, "9929830df9c80e23f33f34f6570a74cd");
	}
	
}
