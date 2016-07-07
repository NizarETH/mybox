package com.paperpad.mybox.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.Map;

@DatabaseTable(tableName = "active_languages")
public class LanguageString {

	
	@DatabaseField(generatedId = true)
	int id;
	
	@DatabaseField(foreign= true, foreignAutoCreate = true, foreignAutoRefresh = true)
	private Console console;
	
	@DatabaseField(canBeNull = false, dataType = DataType.STRING)
	private String string;
	
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
	 * @return the score
	 */
	public Console getScore() {
		return console;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(Console score) {
		this.console = score;
	}

	/**
	 * @return the string
	 */
	public String getString() {
		return string;
	}

	/**
	 * @param string the string to set
	 */
	public void setString(String string) {
		this.string = string;
	}

	public LanguageString(Console console, String string) {
		super();
		this.console = console;
		this.string = string;
	}

	public LanguageString() {
		super();
		// TODO Auto-generated constructor stub
	}




}
