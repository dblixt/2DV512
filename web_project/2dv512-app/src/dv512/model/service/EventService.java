package dv512.model.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ektorp.CouchDbConnector;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.javadocmd.simplelatlng.window.RectangularWindow;

import dv512.controller.util.NosqlManager;
import dv512.model.Event;
import dv512.model.Event2;
import dv512.model.lucene.LuceneConnector;
import dv512.model.lucene.LuceneQuery;

@Named
@ApplicationScoped
public class EventService implements Serializable {

	private static final long serialVersionUID = 6304091400093366529L;

	@Inject 
	private NosqlManager mgr;
	
	public void create(Event event) {
		CouchDbConnector c = mgr.getConnection();
		c.create(event);
	}
	
	public void create(Event2 event)  {
		CouchDbConnector c = mgr.getConnection();
		c.create(event);
	}
	
	
	public Event get(String id)  {
		CouchDbConnector c = mgr.getConnection();
		return c.get(Event.class, id);
	}
	
	public void update(Event event) {
		CouchDbConnector c = mgr.getConnection();
		c.update(event);
	}
	
	
	public List<Event2> find(LatLng origin, double radiusKm) throws IOException {
		// create a rectangular window for query parameters.
		RectangularWindow window = new RectangularWindow(origin, 10, LengthUnit.KILOMETER);
		
		LuceneConnector lc = new LuceneConnector(mgr.getConnection());
			
		LuceneQuery q = new LuceneQuery();
		q.designDocId("_design/geo");
		q.indexName("_search/events");
		q.includeDocs(true);
		q.limit(100);
		q.sort("\"-date\"");
		q.query("lng:[" + window.getLeftLongitude() 
				+ " TO " 
				+ window.getRightLongitude() 
				+ "] AND lat:[" 
				+ window.getMinLatitude() 
				+ " TO " + window.getMaxLatitude()
				+ "]"
		);
		
	
		List<Event2> events = lc.queryIndex(q, Event2.class);
		
		for(Event2 e : events) {
			System.out.println("name: " + e.getTitle());
		}
				
		return null;
	}
	
	
}
