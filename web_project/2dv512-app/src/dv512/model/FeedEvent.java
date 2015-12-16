package dv512.model;

import java.util.ArrayList;
import java.util.List;

public class FeedEvent extends Event {
	
	private List<Dog> dogs;

	public FeedEvent() {
		dogs = new ArrayList<>();
	}
	
	public List<Dog> getDogs() {
		return dogs;
	}

	public void setDogs(List<Dog> dogs) {
		this.dogs = dogs;
	}

}
