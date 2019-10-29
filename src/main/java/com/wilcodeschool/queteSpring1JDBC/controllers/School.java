/**
 * 
 *
 */
package com.wilcodeschool.queteSpring1JDBC.controllers;

/**
 * @author franck Desmedt github/bigmoletos
 *
 */
public class School {

	private int id;
	private String name;
	private String country;
	private int capacity;

	/**
	 * @param name
	 * @param counry
	 * @param capacity
	 */
	public School(int id, String name, String counry, int capacity) {
		this.id = id;
		this.name = name;
		this.country = counry;
		this.capacity = capacity;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the counry
	 */
	public String getCounry() {
		return this.country;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return this.capacity;
	}

}
