package dv512.model.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ektorp.AttachmentInputStream;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;

import dv512.controller.util.NosqlManager;
import dv512.model.nosql.User;

@Named
@ApplicationScoped
public class UserService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private NosqlManager mgr;
	
	public void create(User user) {
		CouchDbConnector c = mgr.getCon();
		c.create(user);
	}
	
	public User get(String id) {
		CouchDbConnector c = mgr.getCon();
		return c.get(User.class, id);
	}
	
	public void update(User user) {
		CouchDbConnector c = mgr.getCon();
		c.update(user);
	}
	
	/**
	 * Verify user email and password in database.
	 * 
	 * @param user only email and password need to be set.
	 * @return the full user object from database on success, null otherwise.
	 */
	public User verify(User user) {
		CouchDbConnector c = mgr.getCon();
		
		ComplexKey key = ComplexKey.of(user.getEmail(), user.getPassword());
		
		ViewQuery q = new ViewQuery()			
				.dbPath(c.getDbInfo().getDbName())
				.viewName("verify")
				.designDocId("_design/user")
				.key(key)
				.limit(1)
				.reduce(false);
		
		System.out.println(q.buildQuery());
		
		try {
			List<User> result = c.queryView(q, User.class);		
			if(!result.isEmpty()) {
				User r = result.get(0);
				r.setPassword(null); // reset password, not needed anymore.
				return r;
			}
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		return null;		
	}
	
	
	public void createAttachment(User userDoc, AttachmentInputStream ais) {
		CouchDbConnector c = mgr.getCon();
		c.createAttachment(userDoc.getId(), userDoc.getRevision(), ais);		
	}
	
}
