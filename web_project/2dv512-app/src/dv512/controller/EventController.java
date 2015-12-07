package dv512.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.dao.CommentsDAO;
import dv512.dao.EventsDAO;
import dv512.model.Comment;
import dv512.model.Event;

@Named
@ViewScoped
public class EventController implements Serializable {

	private static final long serialVersionUID = 6667656806561372380L;

	@Inject
	private EventsDAO eventsDAO;

	@Inject
	private CommentsDAO commentsDAO;

	@Inject
	private LoginController loginController;

	private Event event;

	private List<Comment> comments = new ArrayList<>();

	public Event getEvent() {
		return event;
	}
	

	public List<Comment> getComments() {
		return comments;
	}

	public void loadData() {

		//For testing
		event = eventsDAO.get(1);
		comments = commentsDAO.listAll(1);
		
		if (event == null) {

			System.out.println("Loading profile data!");
			event = eventsDAO.get(event.getId());
			comments = commentsDAO.listAll(event.getId());
		}
	}

	public String saveData() {
		// save changes to database.

		return "profile.xhtml?faces-redirect=true";
	}

}
