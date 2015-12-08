package dv512.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;

@Named
@RequestScoped
public class NosqlController {

	@Resource(name = "couchdb/nosql-app-db")
	protected CouchDbInstance db;

	public void test() {
		System.out.println("Testing nosql");

		CouchDbConnector dbc = db.createConnector("app-db", true);
		Map<String, Object> doc = new HashMap<String, Object>();
		doc.put("doc-type", "user");
		doc.put("email", "daniel.nilsson.9@gmail.com");
		doc.put("password", "secret");
		dbc.create(doc);

	}

}
