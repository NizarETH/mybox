/**
 * 
 */
package com.paperpad.mybox.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author euphordev02
 *
 */
@DatabaseTable(tableName = "illustrations")
public class Illustration {
	
	/**
	 * @param link
	 * @param path
	 * @param fullLink
	 * @param fullPath
	 * @param downloaded
	 */
	public Illustration(String link, String path, String fullLink,
			String fullPath, Boolean downloaded) {
		super();
		this.link = link;
		this.path = path;
		this.fullLink = fullLink;
		this.fullPath = fullPath;
		this.downloaded = downloaded;
	}

	/**
	 * 
	 */
	public Illustration() {
		super();
		// TODO Auto-generated constructor stub
	}
	@DatabaseField(generatedId = true)
	private int id; 
	
	@DatabaseField(dataType = DataType.STRING)
	private String link;
	
	@DatabaseField(dataType = DataType.STRING)
	private String path;
	
	@DatabaseField(dataType = DataType.STRING)
	private String fullLink;
	
	@DatabaseField(dataType = DataType.STRING)
	private String fullPath;
	
	@DatabaseField()
	private int originalHeight;
	
	@DatabaseField()
	private int originalWidth;
	
	@DatabaseField
	private Boolean downloaded;
	
	@DatabaseField(foreign = true)
	private MyBox myBox;
	
	/**
	 * @param id
	 * @param link
	 * @param path
	 * @param downloaded
	 */
	public Illustration(int id, String link, String path, Boolean downloaded) {
		super();
		this.id = id;
		this.link = link;
		this.path = path;
		this.downloaded = downloaded;
	}
	
	/**
	 * @param link
	 * @param path
	 * @param downloaded
	 */
	public Illustration(String link, String path, Boolean downloaded) {
		super();
		this.link = link;
		this.path = path;
		this.downloaded = downloaded;
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
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the downloaded
	 */
	public Boolean getDownloaded() {
		return downloaded;
	}
	/**
	 * @param downloaded the downloaded to set
	 */
	public void setDownloaded(Boolean downloaded) {
		this.downloaded = downloaded;
	}

	/**
	 * @return the originalHeight
	 */
	public int getOriginalHeight() {
		return originalHeight;
	}

	/**
	 * @param originalHeight the originalHeight to set
	 */
	public void setOriginalHeight(int originalHeight) {
		this.originalHeight = originalHeight;
	}

	/**
	 * @return the originalWisth
	 */
	public int getOriginalWidth() {
		return originalWidth;
	}

	/**
	 * @param originalWisth the originalWisth to set
	 */
	public void setOriginalWidth(int originalWisth) {
		this.originalWidth = originalWisth;
	}

	/**
	 * @return the fullLink
	 */
	public String getFullLink() {
		return fullLink;
	}

	/**
	 * @param fullLink the fullLink to set
	 */
	public void setFullLink(String fullLink) {
		this.fullLink = fullLink;
	}

	/**
	 * @return the fullPath
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * @param fullPath the fullPath to set
	 */
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public MyBox getMyBox() {
		return myBox;
	}

	public void setMyBox(MyBox myBox) {
		this.myBox = myBox;
	}

}
