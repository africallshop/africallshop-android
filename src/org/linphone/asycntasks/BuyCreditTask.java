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

public class BuyCreditTask extends AsyncTask<String, Void, Boolean>{
	
	public static String TAG = "BuyCreditTask";
	private ProgressDialog progressBar;
	private int CONNECTION_STATE = 0;
	private String login;
	private String password;
	private String url="";
	Context context;
	private int http_status_code = 200;
	
	private Intent broadcastIntent;
	
	public BuyCreditTask(Context context) {
		this.context = context;
		broadcastIntent = new Intent(Config.INTENT_ACTION);
		progressBar = new ProgressDialog(context);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		login = Utils.getStringFromPreference(context, Config.USER);
		password = Utils.getStringFromPreference(context, Config.PWD);
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
		sendBroadcast(Config.ACTION_GOT_CREDIT_URL, url);
	} 
	
	private boolean mainProcess(){
		RestClient client = new RestClient(Config.API_BUY_CREDIT);
		client.addParam("login", login);
		client.addParam("password", password);
		try{
		    client.execute(RequestMethod.POST);
		    http_status_code = client.getResponseCode();
		    String response = client.getResponse();
//		    Log.e(TAG, response);
		    if(http_status_code == 200){
		    	JSONObject json = new JSONObject(response);
		    	if(json.getString("url") != null ){
		    		url = json.getString("url");
		    		return true;
		    	} 	
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
		
    private void sendBroadcast(int action, String message){
    	broadcastIntent.putExtra("action", action);
    	broadcastIntent.putExtra("message", message);
    	context.sendBroadcast(broadcastIntent);
    }
}
