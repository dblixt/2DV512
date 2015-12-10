package dv512.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class EventController implements Serializable {

	/*
	
	
	private static final long serialVersionUID = 6667656806561372380L;

	@Inject
	private EventsDAO eventsDAO;

	@Inject
	private ProfilesDAO profilesDAO;

	@Inject
	private CommentsDAO commentsDAO;

	@Inject
	private DogsDAO dogsDAO;

	@Inject
	private LoginController loginController;

	private Event event;
	private Profile creator;

	private Comment comment;

	private List<Profile> profiles = new ArrayList<>();
	private List<Comment> comments = new ArrayList<>();
	private List<Dog> dogs = new ArrayList<>();

	public Event getEvent() {
		return event;
	}

	public Comment setComment() {
		return comment;
	}

	public Comment getComment() {
		return comment;
	}

	public Profile getProfile() {
		return creator;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public List<Dog> getDogs() {
		return dogs;
	}

	public void loadData() {


		// For testing
		event = eventsDAO.get(1);
		comments = commentsDAO.listAll(1);
		creator = profilesDAO.get(1);
		dogs = dogsDAO.listAll(1);
		profiles = profilesDAO.listAllEvent(1);

		if (comment == null) {
			comment = new Comment();
			//comment.setUserID(loginController.getUserId());
			comment.setEventId(event.getId());
		}

	}

	public String saveData() {

		// save changes to database.
		comment.setDateTime(System.currentTimeMillis());
		commentsDAO.insert(comment);

		return "eventview.xhtml?faces-redirect=true";

	}
	
	*/

}
