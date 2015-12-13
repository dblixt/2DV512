package dv512.model;

import java.util.ArrayList;
import java.util.List;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Event2 extends CouchDbDocument {
	private static final long serialVersionUID = 1L;

	@TypeDiscriminator
	private User creator;	
	private String title;
	private String description;
	private long date;		
	private double longitude;
	private double latitude;
	
	@JsonIgnore
	private double distance;
	
	private List<User> joins = new ArrayList<>();
	
	
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;	
	}

	
	public List<User> getJoins() {
		return joins;
	}

	public void setJoins(List<User> joins) {
		this.joins = joins;
	}


	public static class User {
		private String id;
		private String name;
		private String image;	
		private List<Dog> dogs = new ArrayList<>();

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public List<Dog> getDogs() {
			return dogs;
		}

		public void setDogs(List<Dog> dogs) {
			this.dogs = dogs;
		}
		
	}
	
	public static class Dog {
		private int id;
		private String name;
		private String image;
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getImage() {
			return image;
		}
		
		public void setImage(String image) {
			this.image = image;
		}
	
	}

}
