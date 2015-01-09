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

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;

public class SendSMSTask extends AsyncTask<String, Void, Boolean>{
	
	public static String TAG = "LoginTask";
	private int CONNECTION_STATE = 0;
	private String login, password, numero, message;
	Context context;
	private int http_status_code = 200;
	
	private Intent broadcastIntent;
	
	public SendSMSTask(Context context) {
		this.context = context;
		broadcastIntent = new Intent(Config.INTENT_ACTION);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		login = Utils.getStringFromPreference(context, Config.USER);
		password = Utils.getStringFromPreference(context, Config.PWD);
		numero = params[0];
		message = params[1];
		return mainProcess();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(Boolean result){
		super.onPostExecute(result);
	} 
	
	private boolean mainProcess(){
		RestClient client = new RestClient(Config.API_SEND_SMS_URL);
		client.addParam("login", login);
		client.addParam("password", password);
		client.addParam("numero", numero);
		client.addParam("message", message);
		try{
		    client.execute(RequestMethod.POST);
		    http_status_code = client.getResponseCode();
		    String response = client.getResponse();
//		    Log.e(TAG, http_status_code+"");
		    Log.e(TAG, response);
		    if(http_status_code == 200){
		    	if(response.trim().equals("OK")){
		    		sendBroadcast(Config.ACTION_SEND_SMS);
		    	}else{
		    		sendBroadcast(Config.ACTION_SEND_KO);
		    	}
		    	return true;
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
		sendBroadcast(Config.ACTION_SEND_ERROR);
		return false;
	}
	
    private void sendBroadcast(int action){
    	broadcastIntent.putExtra("action", action);
    	context.sendBroadcast(broadcastIntent);
    }
    
    @Override
    protected void onCancelled() {
        running = false;
    }
    private boolean running = true;
}
