package com.paperpad.mybox.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Class Panier for ordering online
 */
@DatabaseTable(tableName = "cart_items")
public class CartItem {
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String price;
	
	@DatabaseField
	private String quantity;
	
	@DatabaseField
	private String message;
	
	@DatabaseField
	private String first_name;
	
	@DatabaseField
	private String last_name;
	
	@DatabaseField
	private String email;
	
	@DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true)
	private MyBox mybox;
	
	public CartItem() {
		// TODO Auto-generated constructor stub
	}

	public CartItem(MyBox mybox, String firstName, String lastName, String email, String comment, String quantity) {
		this.mybox = mybox;
		this.price = mybox.getPrix().toString();
		this.quantity = quantity;
		this.first_name = firstName;
		this.last_name = lastName;
		this.email = email;
		this.message = comment;
	}

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
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}


	public CartItem(String price, String description, String name) {
		super();
		this.price = price;
		this.message = description;
		this.first_name = name;
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

	/**
	 * @return the mybox
	 */
	public MyBox getMybox() {
		return mybox;
	}

	/**
	 * @param mybox the mybox to set
	 */
	public void setMybox(MyBox mybox) {
		this.mybox = mybox;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public CartItem(String price, String quantity, String message,
			String first_name, String last_name, String email, MyBox mybox) {
		super();
		this.price = price;
		this.quantity = quantity;
		this.message = message;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.mybox = mybox;
	}

	public String toString() {
		return " price : "+price+", quantity : "+quantity+", message : "+message+",  first_name : "+first_name+", last_name : "+last_name+", email :  "+email+", mybox : "+mybox.toString();
	}

}
