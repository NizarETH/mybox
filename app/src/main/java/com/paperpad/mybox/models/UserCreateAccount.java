package com.paperpad.mybox.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



@DatabaseTable(tableName = "users_tmp")
public class UserCreateAccount {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String email;
	
	@DatabaseField
	private String pwd;
	
	@DatabaseField
	private String first_name;
	
	@DatabaseField
	private String last_name;
	
	@DatabaseField
	private String address;
	
	@DatabaseField
	private String complement;
	
	@DatabaseField
	private int postal_code;
	
	@DatabaseField
	private String city;
	
	@DatabaseField
	private int pays;
	
	@DatabaseField
	private int fix_number_indicatif;
	
	@DatabaseField
	private String fix_phone;
	
	@DatabaseField
	private String mobile_phone;
	
	@DatabaseField
	private String mobile_number_indicatif;



	public UserCreateAccount() {
		email = "";
		pwd  = "";
		first_name  = "";
		last_name  = "";
		address  = "";
		complement  = "";
		city  = "";
		fix_phone  = "";
		mobile_phone  = "";
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
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}



	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}



	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}



	/**
	 * @return the complement
	 */
	public String getComplement() {
		return complement;
	}



	/**
	 * @param complement the complement to set
	 */
	public void setComplement(String complement) {
		this.complement = complement;
	}



	/**
	 * @return the postal_code
	 */
	public int getPostal_code() {
		return postal_code;
	}



	/**
	 * @param postal_code the postal_code to set
	 */
	public void setPostal_code(int postal_code) {
		this.postal_code = postal_code;
	}



	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}



	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}



	/**
	 * @return the pays
	 */
	public int getPays() {
		return pays;
	}



	/**
	 * @param pays the pays to set
	 */
	public void setPays(int pays) {
		this.pays = pays;
	}



	/**
	 * @return the fix_number_indicatif
	 */
	public int getFix_number_indicatif() {
		return fix_number_indicatif;
	}



	/**
	 * @param fix_number_indicatif the fix_number_indicatif to set
	 */
	public void setFix_number_indicatif(int fix_number_indicatif) {
		this.fix_number_indicatif = fix_number_indicatif;
	}



	/**
	 * @return the fix_phone
	 */
	public String getFix_phone() {
		return fix_phone;
	}



	/**
	 * @param fix_phone the fix_phone to set
	 */
	public void setFix_phone(String fix_phone) {
		this.fix_phone = fix_phone;
	}



	/**
	 * @return the mobile_phone
	 */
	public String getMobile_phone() {
		return mobile_phone;
	}



	/**
	 * @param mobile_phone the mobile_phone to set
	 */
	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}



	/**
	 * @return the mobile_number_indicatif
	 */
	public String getMobile_number_indicatif() {
		return mobile_number_indicatif;
	}



	/**
	 * @param mobile_number_indicatif the mobile_number_indicatif to set
	 */
	public void setMobile_number_indicatif(String mobile_number_indicatif) {
		this.mobile_number_indicatif = mobile_number_indicatif;
	}



	public UserCreateAccount(String email, String pwd, String first_name,
			String last_name, String address, String complement,
			int postal_code, String city, int pays, int fix_number_indicatif,
			String fix_phone, String mobile_phone,
			String mobile_number_indicatif) {
		super();
		this.email = email;
		this.pwd = pwd;
		this.first_name = first_name;
		this.last_name = last_name;
		this.address = address;
		this.complement = complement;
		this.postal_code = postal_code;
		this.city = city;
		this.pays = pays;
		this.fix_number_indicatif = fix_number_indicatif;
		this.fix_phone = fix_phone;
		this.mobile_phone = mobile_phone;
		this.mobile_number_indicatif = mobile_number_indicatif;
	}


}
