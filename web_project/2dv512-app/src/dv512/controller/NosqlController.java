package dv512.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.controller.util.NosqlManager;
import dv512.model.nosql.User;

@Named
@RequestScoped
public class NosqlController {

	@Inject
	private NosqlManager nosqlMgr;

	private User user = new User();
	
	
	public void test() {
		System.out.println("Testing nosql");

	}

	
	public User getUser() {
		return user;
	}
	
	public void submit() {
		System.out.println("t<sdg");
	}
	
}
