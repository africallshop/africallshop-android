package org.linphone.utils;

public class Config {
	
	public final static String SERVER_DOMAINE = "https://www.africallshop.com";
	/**
	 * API URLS
	 */
	//login url
	public static String API_LOGIN_URL = SERVER_DOMAINE+"/api/android/auth_account.php";
	public static String API_SET_PROFIL_URL = SERVER_DOMAINE+"/api/android/set_profile.php";
	//contacts url
	public static String API_CONTACTS_URL = SERVER_DOMAINE+"/api/android/get_contacts.php";
	
	public static String API_CALLS_URL = SERVER_DOMAINE+"/api/android/get_calls.php";
	public static String API_SMS_URL = SERVER_DOMAINE+"/api/android/get_sms.php";
	
	public static String API_SEND_SMS_URL = SERVER_DOMAINE+"/api/android/sms.php";
	public static String API_ADD_CONCTACT_URL = SERVER_DOMAINE+"/api/android/add_contact.php";
	public static String API_DELETE_CONTACT_URL = SERVER_DOMAINE+"/api/android/delete_contact.php";
	public static String API_UPDATE_CONTACT_URL = SERVER_DOMAINE+"/api/android/edit_contact.php";
	
	public static String API_OPEN_ACCOUNT  = SERVER_DOMAINE+"/android/creer-un-compte/";
	public static String API_SOLDE  = SERVER_DOMAINE+"/api/android/get_balance.php";
	
	public static String API_CALLBACK  = SERVER_DOMAINE+"/api/android/callback.php";
	public static String API_SUBSCRIBE  = SERVER_DOMAINE+"/api/android/simple_register.php";
	public static String API_CODEVALIDATION  = SERVER_DOMAINE+"/api/android/codevalidation.php";
	public static String API_PRICE  = SERVER_DOMAINE+"/api/android/get_prices.php";
	public static String API_BUY_CREDIT  = SERVER_DOMAINE+"/api/android/buy_credit_send_url.php";
	
//	public static String URL_FORGOTTEN_PASSWORD  = "https://www.africallshop.com/fr/connexion/?motDePasse=1#motDePasse";
	
	
	
	/**
	 * JSON params [{"Username":"9994593181","Password":"450614","Balance":"1.74000","DisplayName":"OSSENI","Server":"sip.africallshop.com:9999","Domain":"sip.africallshop.com:9999"}]
	 * {"numero_portable":"0022508899662","nom":"FARIDA","numero_fixe":null,"numero_africallshop":null}
	 * 
	 * {"Username":"53f852779c789and","Password":"53f8527855b73and","Balance":"0,30 \u20ac","DisplayName":"","Domain":"proxy.africallshop.com","proxy":"proxy.africallshop.com","CallerId":"","firstname":"","lastname":"","zipcode":"","address":"","city":"","country":""}
	 */
	//LOGIN
	public static String JSON_USERNAME = "Username";
	public static String JSON_PASSWORD = "Password";
	public static String JSON_BALANCE = "Balance";
	public static String JSON_DISPLAYNAME = "DisplayName";
	public static String JSON_ADDRESS = "address";
	public static String JSON_DOMAINE = "Domain";
	public static String JSON_CALLERID = "CallerId";
	
	public static String JSON_PROXY = "proxy";
	public static String JSON_FIRSTNAME = "firstname";
	public static String JSON_LASTNAME = "lastname";
	public static String JSON_ZIPCODE = "zipcode";
	public static String JSON_CITY = "city";
	public static String JSON_COUNTRY = "country";
	public static String JSON_STATUS = "status";
	public static String JSON_ID = "id";
	
	// Contact
	public static String JSON_NUM_PORTABLE = "numero_portable";
	public static String JSON_NAME = "nom";
	public static String JSON_NUM_FIXE = "numero_fixe";
	public static String JSON_NUM_AFRICALLSHOP = "numero_africallshop";
	
	//SMS
	public static String JSON_SMS_ID = "id";
	public static String JSON_SMS_DATE = "date_envoi";
	public static String JSON_SMS_DESTINATION = "destination";
	public static String JSON_SMS_PRIX = "prix";
	public static String JSON_SMS_CONTENT = "";
	public static String JSON_SMS_STATUS = "prix";
	
	//CALL
	public static String JSON_CALL_ID = "id";
	public static String JSON_CALL_DATE = "date_appel";
	public static String JSON_CALL_DUREE = "duree_appel";
	public static String JSON_CALL_NUMERO = "numero_appele";
	public static String JSON_CALL_PRIX = "prix_appele";
	public static String JSON_CALL_PAYS = "pays_appele";
	
	//PRICE
	public static String JSON_PRICE = "price";
	public static String JSON_SMS = "sms";
	public static String JSON_COUNTRYCODE = "countrycode";
	public static String JSON_COUNTRYNAME = "countryName";
	public static String JSON_FILENAME = "filename";
	public static String JSON_REMAIND_TIME = "remaind_time";
	
	public static String USER = "user";
	public static String CALLERID = "callerID";
	public static String PWD = "pwd";
	public static String REMEMBER = "remember";
	
	public static String LOGIN = "login";
	public static String PASSWORD = "password";
	
	/**
	 * Timeout params
	 */
	// Set the timeout in milliseconds until a connection is established.
	public static int HTTP_CONNECTION_TIMEOUT = 15; // 30 seconds
	// Set the default socket timeout (SO_TIMEOUT) 
	// in milliseconds which is the timeout for waiting for data.
	public static int HTTP_DATA_TIMEOUT = 30; // 30 seconds
	
	/**
	 * Intent Actions
	 */
	public static String LOCK_PRICE_TASK = "lockAsynchTask";
	
	public static String INTENT_ACTION =  "africallshop.BACKGROUND_PROCESS";
	public static String INTENT_CRUD_CONTACT=  "africallshop.CRUD_CONTACT";
	public static String INTENT_LAUNCHER=  "africallshop.LUNCHER";
	
	public static final int ACTION_LOGGED_IN =  100;
	public static final int ACTION_GOT_CONTACT =  200;
	public static final int ACTION_LAUNCH_PROGRESS_BAR =  300;
	public static final int ACTION_QUIT_PROGRESS_BAR =  400;
	
	
	public static final int ACTION_COMPOSE_NUMBER =  500;
	public static final int ACTION_COMPOSE_SMS =  700;
	public static final int ADD_CONTACT =  800;
	public static final int UPDATE_CONTACT =  900;
	public static final int DELETE_CONTACT =  1000;
	public static final int ACTION_PHONE_VALIDATION =  1100;
	public static final int ACTION_CODE_VALIDATION =  2000;
	public static final int ACTION_SECURITY_POPUP =  1200;
	
	public static final int ACTION_GOT_PRICE =  1300;
	public static final int ACTION_GOT_PRICE_NUMERROR =  1400;
	public static final int ACTION_GOT_PRICE_NETWORKERROR =  1500;
	
	public static final int ACTION_SEND_SMS =  1600;
	public static final int ACTION_SEND_ERROR =  1700;
	public static final int ACTION_SEND_KO =  2000;
	public static final int ACTION_GOT_CREDIT_URL =  1800;
	public static final int ACTION_GOT_PHONE_PRICE =  1900;
	public static final int ACTION_GOT_CODE_VALIDATE =  2100;
	
	public static String NUMBER = "0033601135167";
	
}
