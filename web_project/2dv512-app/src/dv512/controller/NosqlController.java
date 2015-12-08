package dv512.controller;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.ektorp.CouchDbInstance;

@Named
@RequestScoped
public class NosqlController {

	@Resource(name = "couchdb/nosql-app-db")
	protected CouchDbInstance db;
	
	
	
	public void test() {
		System.out.println("Testing nosql");
		
		
	}
	

	
}
