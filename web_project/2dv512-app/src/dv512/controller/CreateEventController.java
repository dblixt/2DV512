package dv512.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dv512.model.Dog;
import dv512.model.Event;
import dv512.model.Profile;
import dv512.model.service.EventService;

@Named
@ViewScoped
public class CreateEventController implements Serializable {

	private static final long serialVersionUID = 6667656806561372380L;

	@Inject
	private EventService eventService;
	@Inject
	private LoginController loginController;
	

	private Event event = new Event();
	
	public Event getEvent() {
		return event;
	}

	public void createEvent() {
		System.out.println("Creating event");
		createTestData();		
		event.setUserId(loginController.getUserId());
		try{
		eventService.create(event);
		System.out.println("Event created");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Failed to create event");
		}
	}
	
	//Just to insert some data into the event for testing
	private void createTestData(){
		List<Dog> dogs = new ArrayList<>();
		List<Profile> profiles = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			Dog dog = new Dog();
			Profile profile = new Profile();
			profile.setName("User"+i);
			profiles.add(profile);
			dog.setAge(i);
			dog.setName("Name"+i);
			dogs.add(dog);
		}
		event.setDogs(dogs);
		event.setProfiles(profiles);
	}


}

