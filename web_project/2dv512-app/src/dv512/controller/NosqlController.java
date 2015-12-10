package dv512.controller;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.sql.DataSource;

import org.ektorp.ActiveTask;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.HttpResponse;

@Named
@RequestScoped
public class NosqlController {

	@Resource(name = "couchdb/nosql-app-db")
	protected CouchDbInstance db;

	public void test() {
		System.out.println("Testing nosql");

		for (String s : db.getAllDatabases()) {
			
			System.out.println(s);

		}
		
		System.out.println("Tinder Dogs DB exists: " + db.checkIfDbExists("tinderdogs"));
		
		if(!db.checkIfDbExists("TestTest")){
			db.createDatabase("TestTest");
		}
		
		for(ActiveTask t: db.getActiveTasks()){
			System.out.println(t.toString());
		}
		
		HttpClient dataSource = db.getConnection();
		HttpResponse response = dataSource.get("https://bc6b3538-baa1-4f1f-9abc-254bf3b6d9af-bluemix.cloudant.com/_all_dbs");
		
		
		

	}

}
