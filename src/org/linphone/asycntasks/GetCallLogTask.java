package org.linphone.asycntasks;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.linphone.utils.Config;
import org.linphone.utils.ConnectionState;
import org.linphone.utils.NetworkUtils;
import org.linphone.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;
	
public class GetCallLogTask extends AsyncTask<String, Void, Boolean>{
	
	public static String TAG = "GetCallLogTask";
	private Intent broadcastIntent;
	private int CONNECTION_STATE = 0;
	private String login;
	private String password; 
	Context context;
	private int http_status_code = 200;
	
	public GetCallLogTask(Context context){   
		this.context = context;
		broadcastIntent = new Intent(Config.INTENT_ACTION);
	}
	
	private boolean loadInBackground(){
		if(!NetworkUtils.IsOnline(context)){
			CONNECTION_STATE = ConnectionState.NO_CONNECTION;
		}else if(!NetworkUtils.PingUrl(Config.SERVER_DOMAINE)){
			CONNECTION_STATE = ConnectionState.SERVER_NOT_AVAILABLE;
		}else{
			login = Utils.getStringFromPreference(context, Config.USER);
			password = Utils.getStringFromPreference(context, Config.PWD);
			return mainProcess();
		}
		return false;
	}
	
	private boolean mainProcess(){
		RestClient client = new RestClient(Config.API_CALLS_URL);
		client.addParam("login", login);
		client.addParam("password", password);
		Log.v(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), "URLS PARAM "+login+" "+password);
		try{
		    client.execute(RequestMethod.POST);
		    http_status_code = client.getResponseCode();
		    //Log.v(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), "STATUS  == "+client.getResponseCode());
		    if(http_status_code == 200){
		    	handleData(client.getResponse().split("=")[1].trim());
		    	return true;
		    }else{
		    	http_error_status_handler(http_status_code,client.getResponse());
		    }
		}catch(ConnectionPoolTimeoutException e){
			CONNECTION_STATE = ConnectionState.TIMEOUT;
		}catch(ConnectTimeoutException e){	
			CONNECTION_STATE = ConnectionState.TIMEOUT;
		}catch(SocketTimeoutException e){
			CONNECTION_STATE = ConnectionState.TIMEOUT;
		}catch(Exception e){
			CONNECTION_STATE = ConnectionState.INTERNAL_ERROR;
//			Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), " HTTP Error "+e.toString());
		}
		return false;  
	}
	
	private void handleData(String json_string){
		Log.v(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), json_string);
		try{
			JSONArray callLogs = new JSONArray(json_string);
			Log.v(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), ""+callLogs.length());
			for(int i =0 ; i< callLogs.length(); i++ ){
//				MyCall c  = new MyCall().setFromJSON(callLogs.getJSONObject(i)); 
//				if(!c.exist(context))
//					c.insert(context);
			}
		}catch (JSONException e){
			CONNECTION_STATE = ConnectionState.INTERNAL_ERROR;
//			Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), "JSON  Error  "+e.toString());
		}
	}
	
	private void http_error_status_handler(int http_status,String message){
//		Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), message);
		switch (http_status){
			case 503:
				//Toast.makeText(context, "Call:"+"not authorized", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
	}
	
    private void sendBroadcast(int action, String message){
    	broadcastIntent.putExtra("action", action);
    	broadcastIntent.putExtra("message", message);
    	context.sendBroadcast(broadcastIntent);
    }
    
	@Override
	protected Boolean doInBackground(String... params){
		return loadInBackground();
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
	}
	
	@Override
	protected void onPostExecute(Boolean result){
		super.onPostExecute(result);
		
		switch (CONNECTION_STATE){
			case ConnectionState.NO_CONNECTION:
//				Toast.makeText(context, "Call:"+context.getString(R.string.error_no_wifi), Toast.LENGTH_LONG).show();
				break;
			case ConnectionState.SERVER_NOT_AVAILABLE:
//				Toast.makeText(context, "Call:"+context.getString(R.string.error_sever_not_available), Toast.LENGTH_LONG).show();
				break;
			case ConnectionState.TIMEOUT:
//				Toast.makeText(context, "Call:"+context.getString(R.string.error_timetout), Toast.LENGTH_LONG).show();
				break;
			case ConnectionState.INTERNAL_ERROR:
//				Toast.makeText(context, "Call:"+context.getString(R.string.error_failure), Toast.LENGTH_LONG).show();
				break;
			default:
				break;
		}
		sendBroadcast(Config.ACTION_QUIT_PROGRESS_BAR, "");
	}
	
}
