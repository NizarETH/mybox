/**
 * 
 */
package com.paperpad.mybox.util.jsonUtils;

import java.util.ArrayList;

/**
 * @author euphordev02
 *
 */
public class AppSession {
	//Fields
	public int application_id;
	public String application_mode;
	public String application_type;
	public String application_unique_identifier;
	public String application_version;
	public String application_push_token;
	public String device_manufacturer;
	public String device_model;
	public String device_platform;
	public String device_screen_resolution;
	public int device_screen_size;
	public String device_system_version;
	public String device_type;
	public String session_unique_identifier;
	public Long start;
	public Long end;
	public ArrayList<AppHit> hits;
	/**
	 * @param application_id
	 * @param application_mode
	 * @param application_type
	 * @param application_unique_identifier
	 * @param application_version
	 * @param application_push_token
	 * @param device_manufacturer
	 * @param device_model
	 * @param device_platform
	 * @param device_screen_resolution
	 * @param device_screen_size
	 * @param device_system_version
	 * @param device_type
	 * @param session_unique_identifier
	 * @param start
	 * @param end
	 * @param hits
	 */
	public AppSession(int application_id, String application_mode,
			String application_type, String application_unique_identifier,
			String application_version, String application_push_token,
			String device_manufacturer, String device_model,
			String device_platform, String device_screen_resolution,
			int device_screen_size, String device_system_version,
			String device_type, String session_unique_identifier, Long start,
			Long end, ArrayList<AppHit> hits) {
		super();
		this.application_id = application_id;
		this.application_mode = application_mode;
		this.application_type = application_type;
		this.application_unique_identifier = application_unique_identifier;
		this.application_version = application_version;
		this.application_push_token = application_push_token;
		this.device_manufacturer = device_manufacturer;
		this.device_model = device_model;
		this.device_platform = device_platform;
		this.device_screen_resolution = device_screen_resolution;
		this.device_screen_size = device_screen_size;
		this.device_system_version = device_system_version;
		this.device_type = device_type;
		this.session_unique_identifier = session_unique_identifier;
		this.start = start;
		this.end = end;
		this.hits = hits;
	}
	
	
}
