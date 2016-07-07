/**
 * 
 */
package com.paperpad.mybox.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author euphordev02
 *
 */
@DatabaseTable(tableName = "beneficiaries")
public class Beneficiary {
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String message;
	
	@DatabaseField
	private String first_name;
	
	@DatabaseField
	private String last_name;
	
	@DatabaseField
	private String email;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}

	/**
	 * @param first_name the first_name to set
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 * @param last_name the last_name to set
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public Beneficiary(String first_name, String last_name, String email,
			String message) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.message = message;
	}

	public Beneficiary() {
		super();
		first_name = "";
		last_name = "";
		email = "";
		message = "";
	}
	
	

}
