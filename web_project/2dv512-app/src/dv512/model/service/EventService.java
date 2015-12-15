package dv512.model.service;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ektorp.CouchDbConnector;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.javadocmd.simplelatlng.window.RectangularWindow;

import dv512.controller.util.NosqlManager;
import dv512.model.Event;
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
			
	public Event get(String id)  {
		CouchDbConnector c = mgr.getConnection();
		return c.get(Event.class, id);
	}
	
	public void update(Event event) {
		CouchDbConnector c = mgr.getConnection();
		c.update(event);
	}
	
	
	
	public List<Event> find(LatLng origin, double radiusKm) throws IOException {		
		try {
			LuceneConnector lc = new LuceneConnector(mgr.getConnection());
			
			LuceneQuery q = buildFindQuery(origin, radiusKm);
			System.out.println(q.buildQuery());
			
			List<Event> events = lc.queryIndex(q, Event.class);			
			Iterator<Event> itr = events.iterator();
			
			// calculate distance for all results, remove those who
			// are outside or search radius.
			while(itr.hasNext()) {
				Event e = itr.next();			
				LatLng location = new LatLng(e.getLatitude(), e.getLongitude());	
				
				double dist = LatLngTool.distance(origin, location, LengthUnit.KILOMETER);
				if(dist > radiusKm) {
					itr.remove();
					continue;
				}
				else {
					e.setDistance(dist);
				}
											
				System.out.println("name: " + e.getTitle() + " distance: " + e.getDistance() + " km");
			}		
			
			return events;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	private LuceneQuery buildFindQuery(LatLng origin, double radiousKm) {
		// create a rectangular window for query parameters.
		RectangularWindow window = new RectangularWindow(
				origin, 2*radiousKm, LengthUnit.KILOMETER);
		
		// show events that are at most 1 day old.
		long dateLowerBound = Instant.now().getEpochSecond() - (24*3600);
		
		LuceneQuery q = new LuceneQuery();
		q.dbPath(mgr.getConnection().getDatabaseName());
		q.designDocId("_design/geo");
		q.indexName("_search/events");
		q.includeDocs(true);
		q.limit(100);
		q.sort("\"date\"");
		q.query("lng:[" + window.getLeftLongitude() 
				+ " TO " 
				+ window.getRightLongitude() 
				+ "] AND lat:[" 
				+ window.getMinLatitude() 
				+ " TO " + window.getMaxLatitude()
				+ "] AND date:["
				+ dateLowerBound
				+ " TO INFINITY ]"
		);
		
		return q;
	}
	
}
