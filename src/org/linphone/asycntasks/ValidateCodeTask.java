package org.linphone.asycntasks;

import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.json.JSONObject;
import org.linphone.R;
import org.linphone.utils.Config;
import org.linphone.utils.ConnectionState;
import org.linphone.utils.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.tylersmith.net.RequestMethod;
import com.tylersmith.net.RestClient;

public class ValidateCodeTask extends AsyncTask<String, Void, Boolean>{
	
	public static String TAG = "InscriptionTask";
	private ProgressDialog progressBar;
	private int CONNECTION_STATE = 0;
	Context context;
	private int http_status_code = 200;
	private String msg= "", login, password;
	private Intent broadcastIntent;

	
	public ValidateCodeTask(Context context) {
		this.context = context;
		broadcastIntent = new Intent(Config.INTENT_ACTION);
		progressBar = new ProgressDialog(context);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
				return mainProcess(params);
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
		progressBar.dismiss();
		if(!result){
			if(msg.equals("KO")){
				new AlertDialog.Builder(context)
		        .setMessage(context.getString(R.string.validate_code_ko))
		        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();						
					}
				})
		        .show();
			}else{
				new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.error_failure))
		        .setMessage(msg)
		        .setNeutralButton(R.string.ok, new android.content.DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which){
						dialog.cancel();						
					}
				})
		        .show();
			}
		}else{
			sendBroadcast(Config.ACTION_GOT_CODE_VALIDATE, login, password);
		}
	}
	
	private RestClient buildClient(String... params){
		RestClient client = new RestClient(Config.API_CODEVALIDATION);
		client.addParam("enregistrerCode", "1");
		client.addParam("language", context.getString(R.string.applang));	
		client.addParam("code", params[0]);
		client.addParam("id", Utils.getStringFromPreference(context, Config.JSON_ID));
//		Log.e("", context.getString(R.string.applang) + "  "+ params[0] + " " +Utils.getStringFromPreference(context, Config.JSON_ID));
		return client; 
	}
	
	private boolean mainProcess(String... params){
		RestClient client = buildClient(params);
		try{
		    client.execute(RequestMethod.POST);
		    http_status_code = client.getResponseCode();
		    String response  = client.getResponse();
//		    Log.e(TAG, client.getResponse());
		    if(http_status_code == 200){
		    	msg = response.trim();
		    	if(response != null && isValidate(response)){
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
			//Log.e(TAG+"->"+new Object(){}.getClass().getEnclosingMethod().getName(), " Error login "+e.toString());
		}
		return false;
	}
	
    private void sendBroadcast(int action, String login, String pass){
    	broadcastIntent.putExtra("action", action);
    	broadcastIntent.putExtra("login", login);
    	broadcastIntent.putExtra("password", pass);
    	context.sendBroadcast(broadcastIntent);
    }

	private boolean isValidate(String json_string){
		try{
			JSONObject json = (JSONObject) new JSONObject(json_string);
			login = json.getString("login");
			password = json.getString("password");
			return true;
		}catch (Exception e){
			
		}
		return false;
	}
}
