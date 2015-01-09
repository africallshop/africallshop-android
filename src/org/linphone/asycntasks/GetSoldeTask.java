package org.linphone.asycntasks;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.R;
import org.linphone.utils.Config;
import org.linphone.utils.ConnectionState;
import org.linphone.utils.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;

public class GetSoldeTask extends AsyncTask<String, Void, Boolean>{
	
	public static String TAG = "GetSoldeTask";
	
	private ProgressDialog progressBar;
	
	private int CONNECTION_STATE = 0;
	private String login;
	private String password; 
	Context context;
	private int http_status_code = 200;
	
	public GetSoldeTask(Context context) {
		this.context = context;
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
		
//		new AlertDialog.Builder(context, R.style.AlertDialogCustom);
		
		new AlertDialog.Builder(context)
        .setTitle("Information")
        .setMessage(context.getText(R.string.balancetext) +" "+Utils.getStringFromPreference(context, Config.JSON_BALANCE))
        .setNegativeButton(R.string.ok, null) 
        .setPositiveButton(R.string.add_credit, new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.cancel();
//				launchWebPage();
				new BuyCreditTask(context).execute("");
			}
		})
        .show();
		
	}
	
	
	private void launchWebPage(){
//		startActivity(new Intent().setClass(LoginActivity.this, WebActivity.class).putExtra(WebActivity.WEBLINK, Config.URL_FORGOTTEN_PASSWORD));
		Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(Config.SERVER_DOMAINE));
		context.startActivity(viewIntent);  
	}
	
	private boolean mainProcess(){
		RestClient client = new RestClient(Config.API_SOLDE);
		client.addParam("login", login);
		client.addParam("password", password);
		try{
		    client.execute(RequestMethod.POST);
		    http_status_code = client.getResponseCode();
//		    Log.e("", client.getResponse().trim());
		    if(http_status_code == 200){
		    	handleData(client.getResponse().trim());
		    	return true;
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
			//Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), " Error login "+e.toString());
			e.printStackTrace();
		}
		return false;
	}
	
	private void handleData(String json_string){
		//Log.v(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), json_string);
		try{
			JSONObject json = new JSONObject(json_string);
			Utils.putStringToPreference(context, Config.JSON_BALANCE, json.getString(Config.JSON_BALANCE));
		}catch (JSONException e){
			CONNECTION_STATE = ConnectionState.INTERNAL_ERROR;
			//Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), " JSON Error  "+e.toString());
		}
	}
	private void http_error_status_handler(int http_status,String message){
		//Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), message);
		switch (http_status) {
			case 503:
				Toast.makeText(context, "not authoraized", Toast.LENGTH_LONG).show();
				break;
	
			default:
				break;
		}
	}
	
}
