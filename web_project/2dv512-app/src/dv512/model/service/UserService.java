package dv512.model.service;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;

import dv512.controller.util.NosqlManager;
import dv512.model.User;

@Named
@ApplicationScoped
public class UserService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject 
	private NosqlManager mgr;
	
	public boolean create(User user) {
		try {
			CouchDbConnector c = mgr.getConnection();
			c.create(user);
			return true;
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		return false;
	}
	
	public User get(String id) {
		CouchDbConnector c = mgr.getConnection();
		return c.get(User.class, id);
	}
	
	public boolean update(User user) {
		try {
			CouchDbConnector c = mgr.getConnection();
			c.update(user);
			return true;
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
		}
		
		return false;
	}
	
	/**
	 * Verify user email and password in database.
	 * 
	 * @param user only email and password need to be set.
	 * @return the full user object from database on success, null otherwise.
	 */
	public User verify(User user) {
		CouchDbConnector c = mgr.getConnection();
		
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
			e.printStackTrace();
			//System.err.println(e.getMessage());
		}
		
		return null;		
	}
	
}
