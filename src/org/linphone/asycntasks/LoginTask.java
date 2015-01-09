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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;

public class LoginTask extends AsyncTask<String, Void, Boolean>{
	
	public static String TAG = "LoginTask";
	private int CONNECTION_STATE = 0;
	private String login;
	private String password; 
	Context context;
	private int http_status_code = 200;
	private boolean disabled = true;
	private boolean loginko = false;
	
	private Intent broadcastIntent;
	private ProgressDialog progressBar;
	
	public LoginTask(Context context) {
		this.context = context;
		broadcastIntent = new Intent(Config.INTENT_ACTION);
		progressBar = new ProgressDialog(context);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if(!NetworkUtils.IsOnline(context)){
			CONNECTION_STATE = ConnectionState.NO_CONNECTION;
		}else if(!NetworkUtils.PingUrl(Config.SERVER_DOMAINE)){
			CONNECTION_STATE = ConnectionState.SERVER_NOT_AVAILABLE;
		}else{
			login = params[0];
			password = params[1];
			return mainProcess();
		}
		return false;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressBar.setMessage(context.getString(R.string.task_status_login));
		this.progressBar.setCanceledOnTouchOutside(false);
		progressBar.show();
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		progressBar.dismiss();
		
		if(!result){
			if(CONNECTION_STATE == ConnectionState.INTERNAL_ERROR){
				Toast.makeText(context, context.getString(R.string.error_failure), Toast.LENGTH_LONG).show();
			}else if(!disabled || loginko){
				new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.error_login_title))
		        .setMessage(context.getString(R.string.error_login))
		        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();						
					}
				})
		        .show();
			}				
		}
//		if(!result){
//			Toast.makeText(context, context.getString(R.string.error_failure), Toast.LENGTH_LONG).show();
//		}
//		if(!result){
//			switch (CONNECTION_STATE) {
//				case ConnectionState.NO_CONNECTION:
//					Toast.makeText(context, context.getString(R.string.error_no_wifi), Toast.LENGTH_LONG).show();
//					break;
//				case ConnectionState.SERVER_NOT_AVAILABLE:
//					Toast.makeText(context, context.getString(R.string.error_sever_not_available), Toast.LENGTH_LONG).show();
//					break;
//				case ConnectionState.TIMEOUT:
//					Toast.makeText(context, context.getString(R.string.error_timetout), Toast.LENGTH_LONG).show();
//					break;
//				case ConnectionState.INTERNAL_ERROR:
//					Toast.makeText(context, context.getString(R.string.error_failure), Toast.LENGTH_LONG).show();
//					break;
//				default:
//					break;
//			}
//		}
	} 
	
	private boolean mainProcess(){
		RestClient client = new RestClient(Config.API_LOGIN_URL);
		client.addParam("login", login);
		client.addParam("password", password);
		try{
		    client.execute(RequestMethod.POST);
		    http_status_code = client.getResponseCode();
		    String response = client.getResponse();
//		    Log.e(TAG, http_status_code+"");
//		    Log.e(TAG, response + "==========");
		    if(http_status_code == 200){
		    	loginko = response.trim().length() <= 0;
		    	if(isEnabled(response)){
		    		return isLogged(response);
		    	} 	
		    }else{
		    	http_error_status_handler(http_status_code,client.getResponse());
		    }
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
	
	
	private boolean isEnabled(String json_string){
		try{
			JSONObject json = new  JSONObject(json_string);
			String status = json.getString(Config.JSON_STATUS);
			int id = Integer.parseInt(json.getString(Config.JSON_ID));
			if(status.equalsIgnoreCase("disabled")){
				Utils.putStringToPreference(context, Config.JSON_ID, ""+id);
				if(id > 0){
					sendBroadcast(Config.ACTION_PHONE_VALIDATION, "");
				}else{
					sendBroadcast(Config.ACTION_SECURITY_POPUP, "");
				}
			}else{
				disabled = false;
			}
			
			return false;
		}catch(Exception e){
			
		}
		return true;
	}
	
	private boolean isLogged(String json_string){
		//Log.v(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(),json_string);
		try{ 
			JSONObject json = (JSONObject) new JSONObject(json_string);
			Log.v(TAG, json.getString(Config.JSON_LASTNAME));
			Utils.putStringToPreference(context, Config.JSON_USERNAME, json.getString(Config.JSON_USERNAME));
			Utils.putStringToPreference(context, Config.JSON_PASSWORD, json.getString(Config.JSON_PASSWORD));
			Utils.putStringToPreference(context, Config.JSON_BALANCE, json.getString(Config.JSON_BALANCE));
			Utils.putStringToPreference(context, Config.JSON_DISPLAYNAME, json.getString(Config.JSON_DISPLAYNAME));
			Utils.putStringToPreference(context, Config.JSON_ADDRESS, json.getString(Config.JSON_ADDRESS));
			Utils.putStringToPreference(context, Config.JSON_DOMAINE, json.getString(Config.JSON_DOMAINE));
			Utils.putStringToPreference(context, Config.JSON_CALLERID, json.getString(Config.JSON_CALLERID));
			Utils.putStringToPreference(context, Config.JSON_PROXY, json.getString(Config.JSON_PROXY));
			Utils.putStringToPreference(context, Config.JSON_FIRSTNAME, json.getString(Config.JSON_FIRSTNAME));
			Utils.putStringToPreference(context, Config.JSON_LASTNAME, json.getString(Config.JSON_LASTNAME));
			Utils.putStringToPreference(context, Config.JSON_ZIPCODE, json.getString(Config.JSON_ZIPCODE));
			Utils.putStringToPreference(context, Config.JSON_CITY, json.getString(Config.JSON_CITY));
			Utils.putStringToPreference(context, Config.JSON_COUNTRY, json.getString(Config.JSON_COUNTRY));
			
			Utils.putStringToPreference(context, Config.USER, login);
			Utils.putStringToPreference(context, Config.PWD, password);
			Utils.putStringToPreference(context, "loggout", "yes");
			sendBroadcast(Config.ACTION_LOGGED_IN, "");
			return true;
		}catch (Exception e){
//			CONNECTION_STATE = ConnectionState.INTERNAL_ERROR;
//			Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), " Error accessing login params "+e.toString());
		}
		return false;
	}
	
	private void http_error_status_handler(int http_status,String message){
		//Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), message);
		switch (http_status) {
			case 503:
				new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.error_login_title))
		        .setMessage(context.getString(R.string.error_login))
		        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();						
					}
				})
		        .show();
				break;
	
			default:
				Toast.makeText(context, context.getString(R.string.error_failure), Toast.LENGTH_LONG).show();
				break;
		}
	}
	
    private void sendBroadcast(int action, String message){
    	broadcastIntent.putExtra("action", action);
    	broadcastIntent.putExtra("message", message);
    	context.sendBroadcast(broadcastIntent);
    }
}
