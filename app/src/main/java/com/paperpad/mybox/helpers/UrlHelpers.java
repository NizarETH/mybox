/**
 * 
 */
package com.paperpad.mybox.helpers;

/**
 * @author euphordev02
 *
 */
public class UrlHelpers {
	
	//http://consolemybox.apicius.com/services_ios/
	/**
	 * console id 
	 */
	public static String CONSOLE_ID =  "6424d438767cdfc5d58621d98f5c0f50";//"12345"; //"60b1a93aa5681eaf431a66368f2c00a8";//1e4b1415861fd32ff15a8ea7442164e1; 60b1a93aa5681eaf431a66368f2c00a8; "749d951977e6bcb19c755c64ec2fe099"; //

    /**
     * url_server for push
     */
    public static final String SERVER_URL = "http://backoffice.paperpad.fr/api/statistic/save";

    /**
	 * to add language to the output json from the server - french
	 */
	public static final String LANG_EXTRA_FR = "&langue=fr";
	
	/**
	 * to add language to the output json from the server - english UK
	 */
	public static final String LANG_EXTRA_EN = "&langue=en";
	
	
	/**
	 * to add language to the output json from the server - english united states
	 */
	
	public static final String LANG = "LANG";
	
	
	public static String LANG_EXTRA = "&langue=fr";



	public static String GET_BASE_URL = "http://consolemybox.apicius.com/services_ios/";//"http://adminmyboxdev.apicius.com/services_ios/"; //

	public static String GET_CREAT_ACCOUNT_URL = GET_BASE_URL+"user_create?console_id="+CONSOLE_ID+LANG_EXTRA;
	
	public static String GET_AUTHENTICATION_URL = GET_BASE_URL+"authentificate?console_id="+CONSOLE_ID+LANG_EXTRA;
	
	public static String GET_ORDER_HISTORY_URL = GET_BASE_URL+"command_history?console_id="+CONSOLE_ID+LANG_EXTRA;

	public static String GET_NOTIFICATION_ORDER_URL = GET_BASE_URL+"relance_invite?console_id="+CONSOLE_ID+LANG_EXTRA;

	/**
	 * url de base des requetes console serveur MyBox
	 */
	public static String GET_CONSOLE_URL = GET_BASE_URL+"get_console?console_id="+CONSOLE_ID+LANG_EXTRA;
	
	/**
	 * url de base des requetes console CATEGORIES serveur MyBox
	 */
	public static String GET_CATEGORIES_URL = GET_BASE_URL+"get_categorie_console?console_id="+CONSOLE_ID+LANG_EXTRA;
	
	/**
	 * url de base des requetes console COFFRETS serveur MyBox
	 */
	public static String GET_COFFRET_URL = GET_BASE_URL+"get_coffret?console_id="+CONSOLE_ID+LANG_EXTRA;
	
	/**
	 * url de base des requetes console LAST UPDATE date serveur MyBox
	 */
	public static String GET_LAST_UPDATE_URL = GET_BASE_URL+"get_lastdate_console?console_id="+CONSOLE_ID+LANG_EXTRA;
	
	
	
	
	/**
	 * 
	 */
	public static String TEST_URL = GET_BASE_URL+"get_console?console_id="+CONSOLE_ID+LANG_EXTRA; // GET_BASE_URL+"get_console?console_id=60b1a93aa5681eaf431a66368f2c00a8&langue=fr";
	
	public static String SVAE_COMMANDE = GET_BASE_URL+"save_commande"+"?console_id="+CONSOLE_ID+LANG_EXTRA;//http://consolemybox.apicius.com/services_ios/save_commande

	
	public static void updateUrlHelpers() {
		
		 GET_CREAT_ACCOUNT_URL = GET_BASE_URL+"user_create?console_id="+CONSOLE_ID+LANG_EXTRA;
		
		 GET_AUTHENTICATION_URL = GET_BASE_URL+"authentificate?console_id="+CONSOLE_ID+LANG_EXTRA;
		
		 GET_ORDER_HISTORY_URL = GET_BASE_URL+"command_history?console_id="+CONSOLE_ID+LANG_EXTRA;

		 GET_NOTIFICATION_ORDER_URL = GET_BASE_URL+"relance_invite?console_id="+CONSOLE_ID+LANG_EXTRA;

		 GET_CONSOLE_URL = GET_BASE_URL+"get_console?console_id="+CONSOLE_ID+LANG_EXTRA;
		

		 GET_CATEGORIES_URL = GET_BASE_URL+"get_categorie_console?console_id="+CONSOLE_ID+LANG_EXTRA;
		
		 GET_COFFRET_URL = GET_BASE_URL+"get_coffret?console_id="+CONSOLE_ID+LANG_EXTRA;
		
		 GET_LAST_UPDATE_URL = GET_BASE_URL+"get_lastdate_console?console_id="+CONSOLE_ID+LANG_EXTRA;

		 SVAE_COMMANDE = GET_BASE_URL+"save_commande"+"?console_id="+CONSOLE_ID+LANG_EXTRA;
	}
}
