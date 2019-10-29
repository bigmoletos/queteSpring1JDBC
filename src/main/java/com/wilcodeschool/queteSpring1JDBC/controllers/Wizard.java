/**
 * 
 *
 */
package com.wilcodeschool.queteSpring1JDBC.controllers;

/**
 * @author franck Desmedt github/bigmoletos
 *
 */
public class Wizard {
	private int id;
	private String firstname;
	private String lastname;

	/**
	 * @param id
	 * @param firstname
	 * @param lastname
	 */
	public Wizard(int id, String firstname, String lastname) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return this.lastname;
	}

	public int getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}
}
