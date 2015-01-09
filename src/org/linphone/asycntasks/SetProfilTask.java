package org.linphone.asycntasks;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.json.JSONObject;
import org.linphone.R;
import org.linphone.utils.Config;
import org.linphone.utils.ConnectionState;
import org.linphone.utils.NetworkUtils;
import org.linphone.utils.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;

public class SetProfilTask extends AsyncTask<String, Void, Boolean>{
	
	public static String TAG = "SetProfilTask";
	private ProgressDialog progressBar;
	private int CONNECTION_STATE = 0;
	Context context;
	private int http_status_code = 200;
	private String login, password, firstname, lastname, zipcode, address, country,callerid;
	private boolean ok= false;
	
	public SetProfilTask(Context context) {
		this.context = context;
		progressBar = new ProgressDialog(context);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		login = Utils.getStringFromPreference(context, Config.USER);
		password = Utils.getStringFromPreference(context, Config.PWD);
		firstname = params[0];
		lastname = params[1];
		zipcode = params[2];
		address = params[3];
		country = params[4];
		callerid = params[5];
		return mainProcess();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressBar.setMessage(context.getString(R.string.task_status_processing));
		progressBar.show();
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(progressBar.isShowing())
			progressBar.dismiss();
		if(result){
			if(!ok){
				Toast.makeText(context, context.getString(R.string.error_set_profile), Toast.LENGTH_LONG).show();
			}else{ 
				Toast.makeText(context, context.getString(R.string.task_status_ok), Toast.LENGTH_LONG).show();
				Utils.putStringToPreference(context, Config.JSON_FIRSTNAME, firstname);
				Utils.putStringToPreference(context, Config.JSON_LASTNAME, lastname);
				Utils.putStringToPreference(context, Config.JSON_ZIPCODE, zipcode);
				Utils.putStringToPreference(context, Config.JSON_ADDRESS, address);
				Utils.putStringToPreference(context, Config.JSON_COUNTRY, country);				
			}
		}else{
			Toast.makeText(context, context.getString(R.string.error_failure), Toast.LENGTH_LONG).show();
		}
	} 
	
	private boolean mainProcess(){
		RestClient client = new RestClient(Config.API_SET_PROFIL_URL);
		client.addParam("login", login);
		client.addParam("password", password);
		client.addParam("firstname", firstname);
		client.addParam("lastname", lastname);
		client.addParam("zipcode", zipcode);
		client.addParam("address", address);
		client.addParam("country", country);
		client.addParam("callerid", callerid);
		try{
		    client.execute(RequestMethod.POST);
		    http_status_code = client.getResponseCode();
		    String response = client.getResponse();
//		    Log.e(TAG, response);
		    if(response != null ){
		    	ok = response.trim().equalsIgnoreCase("OK");
//		    	Log.e(TAG, ok+" ");
		    	}
		    	
		    return (http_status_code == 200);
		}catch(ConnectionPoolTimeoutException e) {
			CONNECTION_STATE = ConnectionState.TIMEOUT;
		}catch(ConnectTimeoutException e){	
			CONNECTION_STATE = ConnectionState.TIMEOUT;
		}catch(SocketTimeoutException e){
			CONNECTION_STATE = ConnectionState.TIMEOUT;
		}catch(Exception e){
			CONNECTION_STATE = ConnectionState.INTERNAL_ERROR;
//			Log.e(TAG, " Error login "+e.toString());
		}
		return false;
	}
		
}
