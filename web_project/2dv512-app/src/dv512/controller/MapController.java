package dv512.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class MapController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3435699365713607739L;

	private double x;
	private double y;

	public double getX() {
		System.out.println("Get X: " + x);
		return x;
	}

	public void setX(double x) {
		System.out.println("Set X: " + x);
		this.x = x;
	}

	public double getY() {
		System.out.println("Get Y: " + y);
		return y;
	}

	public void setY(double y) {
		System.out.println("Set Y: " + y);
		this.y = y;
	}

	public void submit() {

		System.out.println("submit");		
		System.out.println("x: " + x);
		System.out.println("y: " + y);

	}

}
