package dv512.model.service;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ektorp.CouchDbConnector;

import dv512.controller.util.NosqlManager;
import dv512.model.Event;

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
	
	public Event get(String id) {
		CouchDbConnector c = mgr.getConnection();
		return c.get(Event.class, id);
	}
	
	public void update(Event event) {
		CouchDbConnector c = mgr.getConnection();
		c.update(event);
	}
	
	
}
