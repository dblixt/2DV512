package dv512.model;

import java.util.List;

public class FeedEvent {

	private Profile creator;
	private Event event;
	private List<Dog> dogs;
	
	private double distance;
	private int joinCount;

	
	
	public Profile getCreator() {
		return creator;
	}

	public void setCreator(Profile creator) {
		this.creator = creator;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public List<Dog> getDogs() {
		return dogs;
	}

	public void setDogs(List<Dog> dogs) {
		this.dogs = dogs;
	}

	public int getJoinCount() {
		return joinCount;
	}

	public void setJoinCount(int joinCount) {
		this.joinCount = joinCount;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
}
